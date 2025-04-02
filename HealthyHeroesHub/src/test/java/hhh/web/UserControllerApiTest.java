package hhh.web;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import hhh.web.dto.EditProfileRequest;
import hhh.winner.service.WinnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WinnerService winnerService;

    @Autowired
    private MockMvc mockMvc;

    private final UUID testUserId = UUID.randomUUID();
    private final User testUser = createTestUser();

    private User createTestUser() {
        return User.builder()
                .id(testUserId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .phone("1234567890")
                .profilePicture("default.jpg")
                .role(UserRole.USER)
                .registrationDate(LocalDate.now())
                .build();
    }




    @Test
    @WithAnonymousUser
    void getProfileUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/users/{id}/profile", testUserId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }


    @Test
    @WithAnonymousUser
    void getSelfDeleteUserUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/users/delete_profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }


    @Test
    @WithAnonymousUser
    void getWinnerUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/users/winner"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void getProfileAuthenticatedReturnEditProfileView() throws Exception {
        when(userService.getById(testUserId)).thenReturn(testUser);
        mockMvc.perform(get("/users/{id}/profile", testUserId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("editProfileRequest"));
    }

    @Test
    @WithMockUser
    void getWinnerAuthenticatedReturnWinnerView() throws Exception {
        User mockUser = mock(User.class);
        when(userService.getById(any())).thenReturn(mockUser);
        mockMvc.perform(get("/users/winner")
                        .with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("winner"));
    }

    @Test
    @WithAnonymousUser
    void getDeleteProfileUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/users/delete_profile", testUserId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void getDeleteProfileAuthenticatedReturnDeleteProfileView() throws Exception {
        User mockUser = mock(User.class);
        UUID userId = UUID.randomUUID();
        when(userService.getById(userId)).thenReturn(mockUser);
        mockMvc.perform(get("/users/delete_profile")
                        .with(user(new AuthenticationDetails(userId, "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("delete_profile"))
                .andExpect(model().attributeExists("user"));
    }
    @Test
    @WithMockUser
    void editProfileWithValidDataRedirectToHome() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = createTestUser();

        when(userService.getById(userId)).thenReturn(mockUser);

        mockMvc.perform(put("/users/{id}/profile", userId)
                        .param("username", "newusername")
                        .param("email", "new@example.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService).editUser(eq(userId), any(EditProfileRequest.class));
    }

    @Test
    @WithMockUser
    void confirmDeleteUserDeleteUserAndRedirectToRoot() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/users/{id}/deleted", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(userService).deleteUser(userId);
    }
    @Test
    @WithMockUser
    void editProfileWithInvalidDataReturnEditProfileViewWithErrors() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = createTestUser();
        when(userService.getById(userId)).thenReturn(mockUser);

        mockMvc.perform(put("/users/{id}/profile", userId)
                        .param("firstName", "a") // Too short
                        .param("lastName", "b") // Too short
                        .param("email", "invalid-email") // Invalid format
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("editProfileRequest"))
                .andExpect(model().attributeHasFieldErrors("editProfileRequest",
                        "firstName", "lastName", "email"));
        verify(userService, never()).editUser(any(), any());
    }

    @Test
    @WithMockUser
    void editProfileWithValidDataUpdateUserAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = createTestUser();
        when(userService.getById(userId)).thenReturn(mockUser);

        mockMvc.perform(put("/users/{id}/profile", userId)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com")
                        .param("phoneNumber", "1234567890")
                        .param("profilePicture", "https://example.com/image.jpg")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
        verify(userService).editUser(eq(userId), any(EditProfileRequest.class));
    }

    @Test
    @WithAnonymousUser
    void editProfileUnauthenticatedRedirectToLogin() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(put("/users/{id}/profile", userId)
                        .param("firstName", "John")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        verify(userService, never()).editUser(any(), any());
    }
}