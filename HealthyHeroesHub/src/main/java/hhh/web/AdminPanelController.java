package hhh.web;


import ch.qos.logback.core.model.Model;
import hhh.comment.model.Comment;
import hhh.comment.service.CommentService;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.report.model.Report;
import hhh.report.service.ReportService;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
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

    @Autowired
    public AdminPanelController(UserService userService, MealCatalogService mealCatalogService, MealService mealService, ReportService reportService, CommentService commentService) {
        this.userService = userService;
        this.mealCatalogService = mealCatalogService;
        this.mealService = mealService;
        this.reportService = reportService;
        this.commentService = commentService;
    }

    @GetMapping
    public ModelAndView adminPanel(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin_panel");
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);

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
    public ModelAndView changeRole(@PathVariable UUID id) {
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

}
