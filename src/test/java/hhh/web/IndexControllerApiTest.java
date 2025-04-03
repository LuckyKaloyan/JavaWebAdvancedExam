package hhh.web;

import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.model.MealCatalogType;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import hhh.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private MealCatalogService mealCatalogService;

    @Autowired
    private MockMvc mockMvc;

    public User aRandomUser(){
        User user = User.builder()
                .email("random@email.com")
                .registrationDate(LocalDate.now())
                .username("EdinPleshiv")
                .role(UserRole.USER)
                .password("123123")
                .build();
        return user;
    }


    public List<MealCatalog> aRandomMealCatalogs(){
        MealCatalog mealcatalog = MealCatalog.builder()
                .type(MealCatalogType.VEGAN)
                .meals(List.of())
                .owner(aRandomUser())
                .addedOn(LocalDate.now())
                .description("This is a meal catalog")
                .build();
        List<MealCatalog> mealCatalogs = new ArrayList<>();
        mealCatalogs.add(mealcatalog);
        return mealCatalogs;
    }

    @Test
    void getRequestToIndexEndpointReturnIndexView() throws Exception {
     MockHttpServletRequestBuilder request = get("/");
     mockMvc.perform(request).andExpect(status().isOk())
             .andExpect(view().name("index"));
    }

    @Test
    @WithAnonymousUser
    void getRequestToRegisterEndpointReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest","isAuthenticated"));
    }
    @Test
    void postRegister_WithMismatchedPasswords_ShouldShowError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "ValidUser")
                        .param("email", "valid@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("registerRequest", "confirmPassword"));
        verify(userService, never()).register(any());
    }

    @Test
    void postRegister_WithValidData_ShouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "ValidUser")
                        .param("email", "valid@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService).register(any(RegisterRequest.class));
    }

    @Test
    @WithAnonymousUser
    void getRequestToLogInEndpointReturnLogInView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest","isAuthenticated"));
    }


    @Test
    @WithAnonymousUser
    void getRequestToHomeEndpointReturnLogInView() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    @Test
    @WithAnonymousUser
    void getRequestToInfoEndpointReturnInfoView() throws Exception {
        MockHttpServletRequestBuilder request = get("/info");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("info"))
                .andExpect(model().attributeExists("isAuthenticated"));
    }

    @Test
    void getRequestToHomeEndpointReturnHomeView() throws Exception {
        User mockUser = mock(User.class);
        List<MealCatalog> mockCatalogs = mock(ArrayList.class);
        when(userService.getById(any())).thenReturn(mockUser);
        when(mockUser.getMealCatalogs()).thenReturn(mockCatalogs);
        mockMvc.perform(get("/home")
                        .with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user", "allCatalogs"));
    }
    @Test
    void postRequestToRegisterEndpointHappyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .param("username", "TestUser123")
                .param("email", "test@example.com")
                .param("password", "securePassword123")
                .param("confirmPassword", "securePassword123")
                .with(csrf());
        doNothing().when(userService).register(any(RegisterRequest.class));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    void getRequestToLogoutEndpointUnAuthenticated() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    @Test
    void getRequestToLogoutEndpointAuthenticated() throws Exception {
        mockMvc.perform(get("/logout")
                .with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }
}