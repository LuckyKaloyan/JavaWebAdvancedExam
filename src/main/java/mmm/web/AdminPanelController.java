package mmm.web;
import mmm.comment.model.Comment;
import mmm.comment.service.CommentService;
import mmm.meal.model.Meal;
import mmm.meal.service.MealService;
import mmm.mealcatalog.model.MealCatalog;
import mmm.mealcatalog.service.MealCatalogService;
import mmm.report.model.Report;
import mmm.report.service.ReportService;
import mmm.upvote.model.UpVote;
import mmm.upvote.service.UpVoteService;
import mmm.user.model.User;
import mmm.user.model.UserRole;
import mmm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin_panel")
public class AdminPanelController {

    private final UserService userService;
    private final MealCatalogService mealCatalogService;
    private final MealService mealService;
    private final ReportService reportService;
    private final CommentService commentService;
    private final UpVoteService upVoteService;

    @Autowired
    public AdminPanelController(UserService userService, MealCatalogService mealCatalogService, MealService mealService, ReportService reportService, CommentService commentService, UpVoteService upVoteService) {
        this.userService = userService;
        this.mealCatalogService = mealCatalogService;
        this.mealService = mealService;
        this.reportService = reportService;
        this.commentService = commentService;
        this.upVoteService = upVoteService;
    }

    @GetMapping
    public ModelAndView getAdminPanel() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin_panel");
        return modelAndView;
    }

    @GetMapping("/user_management")
    public ModelAndView getUserManagementPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.getAll();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("user_management");
        return modelAndView;
    }
    @PatchMapping("/user_management/change_role/{id}")
    public ModelAndView patchChangeRole(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.getAll();
        modelAndView.addObject("users", users);
        userService.changeRole(id);
        modelAndView.setViewName("user_management");
        return new ModelAndView("redirect:/admin_panel/user_management");
    }


    @GetMapping("/meal_catalog_management")
    public ModelAndView getMealCatalogManagementPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<MealCatalog> mealCatalogs = mealCatalogService.getAllMealCatalogs();
        modelAndView.addObject("mealCatalogs", mealCatalogs);
        modelAndView.setViewName("meal_catalog_management");
        return modelAndView;
    }

    @DeleteMapping("/meal_catalog_management/delete/{id}")
    public ModelAndView deleteMealCatalog(@PathVariable UUID id) {
        mealCatalogService.deleteCatalog(id);
        return new ModelAndView("redirect:/admin_panel/meal_catalog_management");
    }

    @GetMapping("/meal_management")
    public ModelAndView getMealManagementPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<Meal> meals = mealService.getAllMeals();
        modelAndView.addObject("meals", meals);
        modelAndView.setViewName("meal_management");
        return modelAndView;
    }

    @DeleteMapping("/meal_management/delete/{id}")
    public ModelAndView deleteMeal(@PathVariable UUID id) {
        mealService.deleteMealById(id);
        return new ModelAndView("redirect:/admin_panel/meal_management");
    }

    @GetMapping("/report_management")
    public ModelAndView getReportManagementPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<Report> reviewedReports = reportService.getAllReviewedReports();
        List<Report> notReviewedReports = reportService.getAllNotReviewedReports();
        modelAndView.addObject("reviewedReports", reviewedReports);
        modelAndView.addObject("notReviewedReports", notReviewedReports);
        modelAndView.setViewName("report_management");
        return modelAndView;
    }



    @PatchMapping("/report_management/reviewed/{id}")
    public ModelAndView patchReviewedReport(@PathVariable UUID id) {
        reportService.completeReport(id);
        return new ModelAndView("redirect:/admin_panel/report_management");
    }

    @GetMapping("/comment_management")
    public ModelAndView getCommentManagementPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<Comment> comments = commentService.getAllComments();
        modelAndView.addObject("comments", comments);
        modelAndView.setViewName("comment_management");
        return modelAndView;
    }

    @DeleteMapping("/comment_management/delete/{id}")
    public ModelAndView deleteComment(@PathVariable UUID id) {
        commentService.deleteCommentById(id);
        return new ModelAndView("redirect:/admin_panel/comment_management");
    }

    @GetMapping("/users_data")
    public ModelAndView getUsersDataPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.getAll();
        List<User> admins = userService.getAllByRole(UserRole.ADMIN);
        List<User> usersDayAgo = userService.getAllRegisteredLast24hours();
        List<User> usersWeekAgo = userService.getAllRegisteredLastWeek();
        List<User> usersMonthAgo = userService.getAllRegisteredLastMonth();
        List<User> usersYearAgo = userService.getAllRegisteredLastYear();
        modelAndView.addObject("users", users);
        modelAndView.addObject("admins", admins);
        modelAndView.addObject("usersDayAgo", usersDayAgo);
        modelAndView.addObject("usersWeekAgo", usersWeekAgo);
        modelAndView.addObject("usersMonthAgo", usersMonthAgo);
        modelAndView.addObject("usersYearAgo", usersYearAgo);
        modelAndView.setViewName("users_data");
        return modelAndView;
    }
    @GetMapping("/meals_data")
    public ModelAndView getMealsDataPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<Meal> meals = mealService.getAllMeals();
        List<MealCatalog> mealCatalogs = mealCatalogService.getAllMealCatalogs();
        List<Meal> mealsDayAgo = mealService.getAllAddedLast24hours();
        List<Meal> mealsWeekAgo = mealService.getAllAddedLastWeek();
        List<Meal> mealsMonthAgo = mealService.getAllAddedLastMonth();
        List<Meal> mealsYearAgo = mealService.getAllAddedLastYear();
        modelAndView.addObject("meals", meals);
        modelAndView.addObject("mealCatalogs", mealCatalogs);
        modelAndView.addObject("mealsDayAgo", mealsDayAgo);
        modelAndView.addObject("mealsWeekAgo", mealsWeekAgo);
        modelAndView.addObject("mealsMonthAgo", mealsMonthAgo);
        modelAndView.addObject("mealsYearAgo", mealsYearAgo);
        modelAndView.setViewName("meals_data");
        return modelAndView;
    }
    @GetMapping("/comments_and_up_votes_data")
    public ModelAndView getCommentsAndUpVotesDataPanel() {
        ModelAndView modelAndView = new ModelAndView();
        List<UpVote> upVotes = upVoteService.getAll();
        List<Comment> comments = commentService.getAllComments();
        List<UpVote> upVotesLastDay = upVoteService.getAllDateLast24hours();
        List<UpVote> upVotesLastWeek = upVoteService.getAllDateLastWeek();
        List<UpVote> upVotesLastMonth = upVoteService.getAllDateLastMonth();
        List<UpVote> upVotesLastYear = upVoteService.getAllDateLastYear();
        List<Comment> commentsLastDay = commentService.getAllCreatedOnLast24hours();
        List<Comment> commentsLastWeek = commentService.getAllCreatedOnLastWeek();
        List<Comment> commentsLastMonth = commentService.getAllCreatedOnLastMonth();
        List<Comment> commentsLastYear = commentService.getAllCreatedOnLastYear();
        modelAndView.addObject("upVotes", upVotes);
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("upVotesLastDay", upVotesLastDay);
        modelAndView.addObject("upVotesLastWeek", upVotesLastWeek);
        modelAndView.addObject("upVotesLastMonth", upVotesLastMonth);
        modelAndView.addObject("upVotesLastYear", upVotesLastYear);
        modelAndView.addObject("commentsLastDay", commentsLastDay);
        modelAndView.addObject("commentsLastWeek", commentsLastWeek);
        modelAndView.addObject("commentsLastMonth", commentsLastMonth);
        modelAndView.addObject("commentsLastYear", commentsLastYear);
        modelAndView.setViewName("comments_and_up_votes_data");
        return modelAndView;
    }

    @GetMapping("/reports_data")
    public ModelAndView getReportsDataPanel() {
       ModelAndView modelAndView = new ModelAndView();
       List<Report> reports = reportService.getAll();
       List<Report> reportsReviewed = reportService.getAllReviewedReports();
       List<Report> reportsNotReviewed = reportService.getAllNotReviewedReports();
       List<Report> reportsLastYear = reportService.getAllCreatedLastYear();
       List<Report> reportsLastMonth = reportService.getAllCreatedLastMonth();
       List<Report> reportsLastWeek = reportService.getAllCreatedLastWeek();
       List<Report> reportsLastDay = reportService.getAllCreatedLastDay();
       modelAndView.addObject("reports", reports);
       modelAndView.addObject("reportsReviewed", reportsReviewed);
       modelAndView.addObject("reportsNotReviewed", reportsNotReviewed);
       modelAndView.addObject("reportsLastYear", reportsLastYear);
       modelAndView.addObject("reportsLastMonth", reportsLastMonth);
       modelAndView.addObject("reportsLastWeek", reportsLastWeek);
       modelAndView.addObject("reportsLastDay", reportsLastDay);
       modelAndView.setViewName("reports_data");
        return modelAndView;
    }
    @DeleteMapping("/user_management/delete/{id}")
    public ModelAndView deleteUser(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        userService.deleteUser(id);
        modelAndView.setViewName("user_management");
        return new ModelAndView("redirect:/admin_panel/user_management");
    }
}