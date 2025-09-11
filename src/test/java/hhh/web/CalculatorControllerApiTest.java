package hhh.web;

import hhh.calculator.Activity;
import hhh.calculator.CalculatorService;
import hhh.calculator.Gender;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import hhh.web.dto.CalculatorRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
public class CalculatorControllerApiTest {

    @MockitoBean
    private CalculatorService calculatorService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MealService mealService;



    @Autowired
    private MockMvc mockMvc;

    private final UUID testUserId = UUID.randomUUID();
    private final User testUser = createTestUser();

    private User createTestUser() {
        return User.builder()
                .id(testUserId)
                .username("testuser")
                .dailyCalories(2000.0)
                .role(UserRole.USER)
                .build();
    }

    private CalculatorRequest createValidCalculatorRequest() {
        return CalculatorRequest.builder()
                .weight(70.0)
                .height(175.0)
                .age(30)
                .gender(Gender.MALE)
                .activityLevel(Activity.MODERATELY)
                .build();
    }

    @Test
    @WithAnonymousUser
    void getCalculatorUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/calculator"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void getCalculatorAuthenticatedReturnCalculatorView() throws Exception {
        mockMvc.perform(get("/calculator")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculatorRequest"));
    }

    @Test
    @WithMockUser
    void postCalculatorWithValidDataCalculateAndRedirect() throws Exception {
        CalculatorRequest validRequest = createValidCalculatorRequest();
        double expectedCalories = 2500.0;

        when(calculatorService.calculateDailyCalories(
                validRequest.getWeight(),
                validRequest.getHeight(),
                validRequest.getAge(),
                validRequest.getGender(),
                validRequest.getActivityLevel()))
                .thenReturn(expectedCalories);
        when(userService.getById(testUserId)).thenReturn(testUser);

        mockMvc.perform(post("/calculator")
                        .param("weight", String.valueOf(validRequest.getWeight()))
                        .param("height", String.valueOf(validRequest.getHeight()))
                        .param("age", String.valueOf(validRequest.getAge()))
                        .param("gender", validRequest.getGender().toString())
                        .param("activityLevel", validRequest.getActivityLevel().toString())
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calculator/results"));

        verify(userService).setDailyCalories(eq(testUserId), eq(expectedCalories));
    }

    @Test
    @WithMockUser
    void postCalculatorWithInvalidDataReturnCalculatorViewWithErrors() throws Exception {
        mockMvc.perform(post("/calculator")
                        .param("weight", "5") // Too low
                        .param("height", "20") // Too low
                        .param("age", "0") // Too low
                        .param("gender", "") // Empty
                        .param("activityLevel", "") // Empty
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeHasFieldErrors("calculatorRequest",
                        "weight", "height", "age", "gender", "activityLevel"));

    }

    @Test
    @WithMockUser
    void getCalculatorResultsAuthenticatedReturnResultsView() throws Exception {
        when(userService.getById(testUserId)).thenReturn(testUser);

        mockMvc.perform(get("/calculator/results")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator_results"))
                .andExpect(model().attributeExists("dailyCalories"));
    }

    @Test
    @WithMockUser
    void postIncreaseDailyIntakeIncreaseAndRedirect() throws Exception {
        mockMvc.perform(post("/calculator/increase")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calculator/results"));

        verify(userService).increaseDailyCalories500(testUserId);
    }

    @Test
    @WithMockUser
    void postDecreaseDailyIntakeDecreaseAndRedirect() throws Exception {
        mockMvc.perform(post("/calculator/decrease")
                        .with(user(new AuthenticationDetails(testUserId, "testuser", "password", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calculator/results"));

        verify(userService).decreaseDailyCalories500(testUserId);
    }

    @Test
    @WithAnonymousUser
    void getDidYouEatEnoughTodayUnauthenticatedRedirectToLogin() throws Exception {
        mockMvc.perform(get("/calculator/did_you_eat_enough_today"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

}