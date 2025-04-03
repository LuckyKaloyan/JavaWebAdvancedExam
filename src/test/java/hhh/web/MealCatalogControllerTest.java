package hhh.web;
import hhh.comment.model.Comment;
import hhh.meal.model.Meal;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.model.MealCatalogType;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import hhh.meal.service.MealService;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.upvote.service.UpVoteService;
import hhh.comment.service.CommentService;
import hhh.web.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealCatalogController.class)
public class MealCatalogControllerTest {

    @MockitoBean
    private MealCatalogService mealCatalogService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MealService mealService;

    @MockitoBean
    private UpVoteService upVoteService;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    private User createTestUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .email("test@example.com")
                .role(UserRole.USER)
                .build();
    }

    private MealCatalog createTestMealCatalog() {
        return MealCatalog.builder()
                .id(UUID.randomUUID())
                .name("Test Catalog")
                .description("Test Description")
                .type(MealCatalogType.VEGAN)
                .owner(createTestUser())
                .build();
    }

    private Meal createTestMeal() {
        return Meal.builder()
                .id(UUID.randomUUID())
                .name("Test Meal")
                .description("Test Description")
                .proteins(BigDecimal.TEN)
                .carbs(BigDecimal.TEN)
                .fats(BigDecimal.TEN)
                .picture("http://example.com/meal.jpg")
                .build();
    }

    @Test
    @WithMockUser
    void createNewCatalogReturnCreateCatalogView() throws Exception {
        mockMvc.perform(get("/meal_catalogs/create_new_catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_new_catalog"))
                .andExpect(model().attributeExists("mealCatalogRequest"));
    }

    @Test
    @WithMockUser
    void postNewCatalogWithValidDataRedirectToHome() throws Exception {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("Test Catalog");
        request.setDescription("Test Description");
        request.setType(MealCatalogType.VEGAN);

        User mockUser = createTestUser();
        when(userService.getById(any())).thenReturn(mockUser);

        mockMvc.perform(post("/meal_catalogs/create_new_catalog")
                        .with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER)))
                        .flashAttr("mealCatalogRequest", request)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(mealCatalogService).createMealCatalog(any(MealCatalogRequest.class), any(User.class));
    }

    @Test
    @WithMockUser
    void postNewCatalogWithInvalidDataReturnCreateCatalogView() throws Exception {
        MealCatalogRequest request = new MealCatalogRequest();

        mockMvc.perform(post("/meal_catalogs/create_new_catalog")
                        .flashAttr("mealCatalogRequest", request)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("create_new_catalog"))
                .andExpect(model().attributeHasErrors("mealCatalogRequest"));
    }

    @Test
    @WithMockUser
    void getMealCatalogReturnMealCatalogView() throws Exception {
        UUID catalogId = UUID.randomUUID();
        MealCatalog mockCatalog = createTestMealCatalog();
        User mockUser = createTestUser();

        when(mealCatalogService.getMealCatalogById(catalogId)).thenReturn(mockCatalog);
        when(userService.getById(any())).thenReturn(mockUser);

        mockMvc.perform(get("/meal_catalogs/{id}", catalogId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("meal_catalog"))
                .andExpect(model().attributeExists("mealCatalog", "user"));
    }

    @Test
    @WithMockUser
    void getLeaderboardReturnLeaderboardView() throws Exception {
        User mealOwner = User.builder()
                .id(UUID.randomUUID())
                .username("mealOwner")
                .build();

        Meal testMeal = Meal.builder()
                .id(UUID.randomUUID())
                .name("Test Meal")
                .description("Test Description")
                .owner(mealOwner)
                .upVotes(new ArrayList<>())
                .build();

        User mockUser = createTestUser();
        List<Meal> mockMeals = List.of(testMeal);
        when(mealService.getTop20Meals()).thenReturn(mockMeals);
        when(userService.getById(any(UUID.class))).thenReturn(mockUser);
        mockMvc.perform(get("/meal_catalogs/leaderboard").with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("leaderboard"))
                .andExpect(model().attributeExists("user", "top20meals"))
                .andExpect(model().attribute("top20meals", mockMeals))
                .andExpect(content().string(containsString("Top Meals")))
                .andExpect(content().string(containsString("Test Meal")));
    }

    @Test
    @WithMockUser
    void getFavouriteMealsReturnFavouriteMealsView() throws Exception {
        User mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .email("test@example.com")
                .role(UserRole.USER)
                .favouriteMeals(new ArrayList<>())
                .build();

        when(userService.getById(any(UUID.class))).thenReturn(mockUser);
        mockMvc.perform(get("/meal_catalogs/favourite_meals").with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("favourite_meals"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(containsString("You don't have Favourite Meals")));
    }

    @Test
    @WithMockUser
    void getEditMealCatalogReturnEditCatalogView() throws Exception {
        UUID catalogId = UUID.randomUUID();
        MealCatalog mockCatalog = createTestMealCatalog();
        when(mealCatalogService.getMealCatalogById(catalogId)).thenReturn(mockCatalog);
        mockMvc.perform(get("/meal_catalogs/{id}/edit", catalogId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_catalog"))
                .andExpect(model().attributeExists("mealCatalog", "editCatalogRequest"));
    }

    @Test
    @WithMockUser
    void patchMealCatalogWithValidDataRedirectToHome() throws Exception {
        UUID catalogId = UUID.randomUUID();
        EditCatalogRequest request = new EditCatalogRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        mockMvc.perform(patch("/meal_catalogs/{id}/edit", catalogId)
                        .flashAttr("editCatalogRequest", request)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(mealCatalogService).editMealCatalog(eq(catalogId), any(EditCatalogRequest.class));
    }

    @Test
    @WithMockUser
    void patchMealCatalogWithInvalidDataReturnEditCatalogView() throws Exception {
        UUID catalogId = UUID.randomUUID();
        EditCatalogRequest request = new EditCatalogRequest();
        MealCatalog mockCatalog = createTestMealCatalog();
        when(mealCatalogService.getMealCatalogById(catalogId)).thenReturn(mockCatalog);
        mockMvc.perform(patch("/meal_catalogs/{id}/edit", catalogId)
                        .flashAttr("editCatalogRequest", request)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_catalog"))
                .andExpect(model().attributeHasErrors("editCatalogRequest"));
    }

    @Test
    @WithMockUser
    void deleteMealCatalogRedirectToHome() throws Exception {
        UUID catalogId = UUID.randomUUID();
        mockMvc.perform(delete("/meal_catalogs/delete/{id}", catalogId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
        verify(mealCatalogService).deleteCatalog(catalogId);
    }

    @Test
    @WithMockUser
    void addNewMealReturnAddMealView() throws Exception {
        UUID catalogId = UUID.randomUUID();
        MealCatalog mockCatalog = createTestMealCatalog();
        when(mealCatalogService.getMealCatalogById(catalogId)).thenReturn(mockCatalog);
        mockMvc.perform(get("/meal_catalogs/{id}/add_meal", catalogId))
                .andExpect(status().isOk())
                .andExpect(view().name("add_meal"))
                .andExpect(model().attributeExists("mealCatalog", "mealRequest"));
    }

    @Test
    @WithMockUser
    void postNewMealWithValidDataRedirectToCatalog() throws Exception {
        UUID catalogId = UUID.randomUUID();
        MealRequest request = new MealRequest();
        request.setName("Test Meal");
        request.setDescription("Test Description");
        request.setProteins(BigDecimal.TEN);
        request.setCarbs(BigDecimal.TEN);
        request.setFats(BigDecimal.TEN);
        request.setPicture("http://example.com/meal.jpg");
        MealCatalog mockCatalog = createTestMealCatalog();
        when(mealCatalogService.getMealCatalogById(catalogId)).thenReturn(mockCatalog);
        mockMvc.perform(post("/meal_catalogs/{id}/add_meal", catalogId)
                        .flashAttr("mealRequest", request)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/" + catalogId));
        verify(mealService).addMeal(any(MealRequest.class), any(MealCatalog.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void getMeal_ShouldReturnCompleteMealView() throws Exception {
        UUID mealId = UUID.randomUUID();
        User mealOwner = User.builder()
                .id(UUID.randomUUID())
                .username("mealOwner")
                .build();
        User mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .build();
        Meal mockMeal = Meal.builder()
                .id(mealId)
                .name("Test Meal")
                .description("Test Description")
                .picture("http://example.com/meal.jpg")
                .proteins(new BigDecimal("25.5"))
                .carbs(new BigDecimal("30.2"))
                .fats(new BigDecimal("15.7"))
                .totalCalories(new BigDecimal("350.0"))
                .addedOn(LocalDate.now())
                .owner(mealOwner)
                .upVotes(new ArrayList<>())
                .favouriteMeals(new ArrayList<>())
                .comments(new ArrayList<>())
                .mealCatalog(MealCatalog.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();
        when(mealService.getMealById(mealId)).thenReturn(mockMeal);
        when(userService.getById(mockUser.getId())).thenReturn(mockUser);
        when(upVoteService.hasUserUpVoted(mealId, mockUser.getId())).thenReturn(false);
        when(mealService.isFavourite(mealId, mockUser.getId())).thenReturn(false);
        mockMvc.perform(get("/meal_catalogs/meal/{id}", mealId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER))))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(model().attributeExists("meal"))
                .andExpect(model().attributeExists("commentRequest"))
                .andExpect(model().attributeExists("hasVoted"))
                .andExpect(model().attributeExists("isFavourite"))
                .andExpect(model().attribute("hasVoted", false))
                .andExpect(model().attribute("isFavourite", false));
    }
    @Test
    @WithMockUser
    void deleteMealRedirectToCatalog() throws Exception {
        UUID catalogId = UUID.randomUUID();
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(delete("/meal_catalogs/{id1}/meal/delete/{id2}", catalogId, mealId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/" + catalogId));
        verify(mealService).deleteMealById(mealId);
    }

    @Test
    @WithMockUser
    void postFavouriteRedirectToMeal() throws Exception {
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(post("/meal_catalogs/meals/add_to_favourite/{id}", mealId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/meal/" + mealId));
        verify(mealService).addFavouriteMeal(eq(mealId), any(UUID.class));
    }

    @Test
    @WithMockUser
    void postUpVoteRedirectToMeal() throws Exception {
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(post("/meal_catalogs/meals/up_vote/{id}", mealId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/meal/" + mealId));
        verify(upVoteService).upVote(eq(mealId), any(UUID.class));
    }

    @Test
    @WithMockUser
    void deleteUpVoteRedirectToMeal() throws Exception {
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(delete("/meal_catalogs/meals/up_vote/{id}", mealId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/meal/" + mealId));
        verify(upVoteService).downVote(eq(mealId), any(UUID.class));
    }

    @Test
    @WithMockUser
    void deleteFavouriteToMealRedirectToMeal() throws Exception {
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(delete("/meal_catalogs/meals/unfavourite/{id}", mealId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/meal/" + mealId));
        verify(mealService).unFavouriteMeal(eq(mealId), any(UUID.class));
    }

    @Test
    @WithMockUser
    void removeFavouriteToHomeRedirectToHome() throws Exception {
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(delete("/meal_catalogs/meals/remove_from_favourite/{id}", mealId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
        verify(mealService).deleteFavouriteMeal(mealId);
    }

    @Test
    @WithMockUser
    void postCommentWithValidDataRedirectToMeal() throws Exception {
        UUID mealId = UUID.randomUUID();
        CommentRequest request = new CommentRequest();
        request.setText("Test comment");

        mockMvc.perform(post("/meal_catalogs/meal/{id}/add_comment", mealId).with(user(new AuthenticationDetails(UUID.randomUUID(), "User123", "123123", UserRole.USER)))
                        .flashAttr("commentRequest", request)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/meal/" + mealId));

        verify(commentService).createComment(anyString(), any(UUID.class), eq(mealId));
    }

    @Test
    @WithMockUser
    void deleteCommentRedirectToMeal() throws Exception {
        UUID mealId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        mockMvc.perform(delete("/meal_catalogs/meal/{id}/delete_comment/{id2}", mealId, commentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meal_catalogs/meal/" + mealId));
        verify(commentService).deleteCommentById(commentId);
    }
}