package hhh.web;

import hhh.comment.model.Comment;
import hhh.meal.model.Meal;
import hhh.mealcatalog.model.MealCatalog;
import hhh.report.model.Report;
import hhh.report.model.ReportLocation;
import hhh.report.model.ReportType;
import hhh.upvote.model.UpVote;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.meal.service.MealService;
import hhh.report.service.ReportService;
import hhh.comment.service.CommentService;
import hhh.upvote.service.UpVoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPanelController.class)
public class AdminPanelControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private MealCatalogService mealCatalogService;
    @MockitoBean
    private MealService mealService;
    @MockitoBean
    private ReportService reportService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private UpVoteService upVoteService;

    @Autowired
    private MockMvc mockMvc;

    private User createTestUser(UserRole role) {
        return User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .role(role)
                .registrationDate(LocalDate.now())
                .build();
    }

    private MealCatalog createTestMealCatalog() {
        return MealCatalog.builder()
                .id(UUID.randomUUID())
                .description("Test Catalog")
                .build();
    }

    private Meal createTestMeal() {
        return Meal.builder()
                .id(UUID.randomUUID())
                .name("Test Meal")
                .build();
    }

    private Report createTestReport() {
        return Report.builder()
                .id(UUID.randomUUID())
                .description("Test Report")
                .build();
    }

    private Comment createTestComment() {
        return Comment.builder()
                .id(UUID.randomUUID())
                .text("Test Comment")
                .build();
    }

    private UpVote createTestUpVote() {
        return UpVote.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAdminPanelReturnAdminPanelView() throws Exception {
        mockMvc.perform(get("/admin_panel"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_panel"));
    }

    @Test
    @WithMockUser
    void getAdminPanelWithoutAdminRoleBeForbidden() throws Exception {
        mockMvc.perform(get("/admin_panel"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getUserManagementPanelReturnUserManagementView() throws Exception {
        List<User> users = List.of(createTestUser(UserRole.USER));
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/admin_panel/user_management"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_management"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void patchChangeRoleChangeRoleAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(patch("/admin_panel/user_management/change_role/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_panel/user_management"));

        verify(userService).changeRole(userId);
    }

    @Test
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    void deleteUserDeleteUserAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();
        User testUser = User.builder()
                .id(userId)
                .username("testUser")
                .email("test@example.com")
                .phone("1234567890")
                .registrationDate(LocalDate.now())
                .role(UserRole.USER)
                .build();
        when(userService.getById(userId)).thenReturn(testUser);
        mockMvc.perform(delete("/admin_panel/user_management/delete/{id}", userId)
                        .with(csrf())
                        .with(request -> {request.setAttribute("userId", userId.toString());return request;
                        }))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_panel/user_management"))
                .andExpect(flash().attributeCount(0));
        verify(userService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getMealCatalogManagementPanelReturnMealCatalogManagementView() throws Exception {
        User testUser = createTestUser(UserRole.USER);
        MealCatalog testCatalog = MealCatalog.builder()
                .id(UUID.randomUUID())
                .owner(testUser)
                .name("Test Catalog")
                .description("Test Description")
                .addedOn(LocalDate.now())
                .build();
        List<MealCatalog> catalogs = List.of(testCatalog);
        when(mealCatalogService.getAllMealCatalogs()).thenReturn(catalogs);

        mockMvc.perform(get("/admin_panel/meal_catalog_management")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("meal_catalog_management"))
                .andExpect(model().attributeExists("mealCatalogs"))
                .andExpect(model().attribute("mealCatalogs", catalogs));
        verify(mealCatalogService, times(1)).getAllMealCatalogs();
        verifyNoMoreInteractions(mealCatalogService);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteMealCatalogDeleteCatalogAndRedirect() throws Exception {
        UUID catalogId = UUID.randomUUID();

        mockMvc.perform(delete("/admin_panel/meal_catalog_management/delete/{id}", catalogId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_panel/meal_catalog_management"));

        verify(mealCatalogService).deleteCatalog(catalogId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getMealManagementPanelReturnMealManagementView() throws Exception {

        User owner = User.builder()
                .id(UUID.randomUUID())
                .username("testOwner")
                .build();

        Meal testMeal = Meal.builder()
                .id(UUID.randomUUID())
                .name("Test Meal")
                .description("Test Description")
                .totalCalories(BigDecimal.valueOf(500))
                .addedOn(LocalDate.now())
                .owner(owner)
                .build();

        List<Meal> meals = List.of(testMeal);
        when(mealService.getAllMeals()).thenReturn(meals);
        mockMvc.perform(get("/admin_panel/meal_management"))
                .andDo(print()) // For debugging
                .andExpect(status().isOk())
                .andExpect(view().name("meal_management"))
                .andExpect(model().attributeExists("meals"))
                .andExpect(model().attribute("meals", meals));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteMealDeleteMealAndRedirect() throws Exception {
        UUID mealId = UUID.randomUUID();
        mockMvc.perform(delete("/admin_panel/meal_management/delete/{id}", mealId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_panel/meal_management"));
        verify(mealService).deleteMealById(mealId);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getReportManagementPanelReturnReportManagementView() throws Exception {
        User concernedUser = User.builder()
                .id(UUID.randomUUID())
                .username("concernedUser")
                .build();
        Report testReport = Report.builder()
                .id(UUID.randomUUID())
                .concernedUser(concernedUser)
                .troublemaker("problemUser")
                .reportType(ReportType.FRAUD)
                .whereItHappened(ReportLocation.COMMENT)
                .description("Inappropriate content")
                .dateOfIssue(LocalDate.now().minusDays(1))
                .createdOn(LocalDate.now())
                .build();

        List<Report> reviewedReports = List.of(testReport);
        List<Report> notReviewedReports = List.of(testReport);

        when(reportService.getAllReviewedReports()).thenReturn(reviewedReports);
        when(reportService.getAllNotReviewedReports()).thenReturn(notReviewedReports);
        mockMvc.perform(get("/admin_panel/report_management"))
                .andExpect(status().isOk())
                .andExpect(view().name("report_management"))
                .andExpect(model().attributeExists("reviewedReports", "notReviewedReports"))
                .andExpect(model().attribute("reviewedReports", reviewedReports))
                .andExpect(model().attribute("notReviewedReports", notReviewedReports));
        verify(reportService, times(1)).getAllReviewedReports();
        verify(reportService, times(1)).getAllNotReviewedReports();
        verifyNoMoreInteractions(reportService);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void patchReviewedReportMarkReportAsReviewedAndRedirect() throws Exception {
        UUID reportId = UUID.randomUUID();

        mockMvc.perform(patch("/admin_panel/report_management/reviewed/{id}", reportId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_panel/report_management"));

        verify(reportService).completeReport(reportId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getCommentManagementPanelReturnCommentManagementView() throws Exception {
        User commentUser = User.builder()
                .id(UUID.randomUUID())
                .username("commentUser123")
                .build();
        Meal commentMeal = Meal.builder()
                .id(UUID.randomUUID())
                .name("Tasty Meal")
                .build();
        Comment testComment = Comment.builder()
                .id(UUID.randomUUID())
                .text("This is a test comment")
                .user(commentUser)
                .meal(commentMeal)
                .createdOn(LocalDate.now())
                .build();
        List<Comment> comments = List.of(testComment);
        when(commentService.getAllComments()).thenReturn(comments);
        mockMvc.perform(get("/admin_panel/comment_management"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment_management"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", comments))
                .andExpect(content().string(containsString("Comment Management")))
                .andExpect(content().string(containsString("Tasty Meal")))
                .andExpect(content().string(containsString("commentUser123")));
        verify(commentService, times(1)).getAllComments();
        verifyNoMoreInteractions(commentService);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteCommentDeleteCommentAndRedirect() throws Exception {
        UUID commentId = UUID.randomUUID();

        mockMvc.perform(delete("/admin_panel/comment_management/delete/{id}", commentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_panel/comment_management"));

        verify(commentService).deleteCommentById(commentId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getUsersDataPanelReturnUsersDataView() throws Exception {
        List<User> users = List.of(createTestUser(UserRole.USER));
        List<User> admins = List.of(createTestUser(UserRole.ADMIN));
        when(userService.getAll()).thenReturn(users);
        when(userService.getAllByRole(UserRole.ADMIN)).thenReturn(admins);
        when(userService.getAllRegisteredLast24hours()).thenReturn(users);
        when(userService.getAllRegisteredLastWeek()).thenReturn(users);
        when(userService.getAllRegisteredLastMonth()).thenReturn(users);
        when(userService.getAllRegisteredLastYear()).thenReturn(users);
        mockMvc.perform(get("/admin_panel/users_data"))
                .andExpect(status().isOk())
                .andExpect(view().name("users_data"))
                .andExpect(model().attributeExists("users", "admins",
                        "usersDayAgo", "usersWeekAgo", "usersMonthAgo", "usersYearAgo"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getMealsDataPanelReturnMealsDataView() throws Exception {
        List<Meal> meals = List.of(createTestMeal());
        List<MealCatalog> catalogs = List.of(createTestMealCatalog());
        when(mealService.getAllMeals()).thenReturn(meals);
        when(mealCatalogService.getAllMealCatalogs()).thenReturn(catalogs);
        when(mealService.getAllAddedLast24hours()).thenReturn(meals);
        when(mealService.getAllAddedLastWeek()).thenReturn(meals);
        when(mealService.getAllAddedLastMonth()).thenReturn(meals);
        when(mealService.getAllAddedLastYear()).thenReturn(meals);
        mockMvc.perform(get("/admin_panel/meals_data"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals_data"))
                .andExpect(model().attributeExists("meals", "mealCatalogs",
                        "mealsDayAgo", "mealsWeekAgo", "mealsMonthAgo", "mealsYearAgo"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getCommentsAndUpVotesDataPanelReturnCommentsAndUpVotesDataView() throws Exception {
        List<Comment> comments = List.of(createTestComment());
        List<UpVote> upVotes = List.of(createTestUpVote());
        when(commentService.getAllComments()).thenReturn(comments);
        when(upVoteService.getAll()).thenReturn(upVotes);
        when(commentService.getAllCreatedOnLast24hours()).thenReturn(comments);
        when(commentService.getAllCreatedOnLastWeek()).thenReturn(comments);
        when(commentService.getAllCreatedOnLastMonth()).thenReturn(comments);
        when(commentService.getAllCreatedOnLastYear()).thenReturn(comments);
        when(upVoteService.getAllDateLast24hours()).thenReturn(upVotes);
        when(upVoteService.getAllDateLastWeek()).thenReturn(upVotes);
        when(upVoteService.getAllDateLastMonth()).thenReturn(upVotes);
        when(upVoteService.getAllDateLastYear()).thenReturn(upVotes);
        mockMvc.perform(get("/admin_panel/comments_and_up_votes_data"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments_and_up_votes_data"))
                .andExpect(model().attributeExists("comments", "upVotes",
                        "commentsLastDay", "commentsLastWeek", "commentsLastMonth", "commentsLastYear",
                        "upVotesLastDay", "upVotesLastWeek", "upVotesLastMonth", "upVotesLastYear"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getReportsDataPanelReturnReportsDataView() throws Exception {
        List<Report> reports = List.of(createTestReport());
        when(reportService.getAll()).thenReturn(reports);
        when(reportService.getAllReviewedReports()).thenReturn(reports);
        when(reportService.getAllNotReviewedReports()).thenReturn(reports);
        when(reportService.getAllCreatedLastYear()).thenReturn(reports);
        when(reportService.getAllCreatedLastMonth()).thenReturn(reports);
        when(reportService.getAllCreatedLastWeek()).thenReturn(reports);
        when(reportService.getAllCreatedLastDay()).thenReturn(reports);
        mockMvc.perform(get("/admin_panel/reports_data"))
                .andExpect(status().isOk())
                .andExpect(view().name("reports_data"))
                .andExpect(model().attributeExists("reports", "reportsReviewed", "reportsNotReviewed",
                        "reportsLastYear", "reportsLastMonth", "reportsLastWeek", "reportsLastDay"));
    }
}