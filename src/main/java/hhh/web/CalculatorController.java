package hhh.web;
import hhh.calculator.CalculatorService;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.meal_tracking.client.dto.UserMealListRequest;
import hhh.meal_tracking.service.MealTrackingService;
import hhh.security.AuthenticationDetails;
import hhh.user.service.UserService;
import hhh.web.dto.CalculatorRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;
    private final UserService userService;
    private final MealService mealService;
    private final MealTrackingService mealTrackingService;


    @Autowired
    public CalculatorController(CalculatorService calculatorService, UserService userService, MealService mealService, MealTrackingService mealTrackingService) {
        this.calculatorService = calculatorService;
        this.userService = userService;
        this.mealService = mealService;
        this.mealTrackingService = mealTrackingService;
    }

    @GetMapping()
    public ModelAndView getCalculator(CalculatorRequest calculatorRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("calculatorRequest",calculatorRequest);
        modelAndView.setViewName("calculator");
        return modelAndView;
    }
    @PostMapping()
    public ModelAndView postCalculator(@Valid CalculatorRequest calculatorRequest, BindingResult bindingResult,@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if(bindingResult.hasErrors()){
            return getCalculator(calculatorRequest);
        }

        double dailyCalories = calculatorService.calculateDailyCalories(
                calculatorRequest.getWeight(),
                calculatorRequest.getHeight(),
                calculatorRequest.getAge(),
                calculatorRequest.getGender(),
                calculatorRequest.getActivityLevel()
        );
        userService.setDailyCalories(authenticationDetails.getId(), dailyCalories);
     return new ModelAndView("redirect:/calculator/results");
    }

    @PostMapping("/increase")
    public String postIncreaseDailyIntake(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        userService.increaseDailyCalories500(authenticationDetails.getId());
        return "redirect:/calculator/results";
    }

    @PostMapping("/decrease")
    public String postDecreaseDailyIntake(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        userService.decreaseDailyCalories500(authenticationDetails.getId());
        return "redirect:/calculator/results";
    }

    @GetMapping("/results")
    public ModelAndView getCalculatorResults(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("calculator_results");
        double dailyCalories = userService.getById(authenticationDetails.getId()).getDailyCalories();
        modelAndView.addObject("dailyCalories",dailyCalories);
        return modelAndView;
    }

    @GetMapping("/did_you_eat_enough_today")
    public ModelAndView getDidYouEatEnoughToday(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("did_you_eat_enough_today");
        List<Meal> allMeals = mealService.getAllMeals();
        UserMealListRequest userMealListRequest = mealTrackingService.getUserMealList(authenticationDetails.getId());
        double totalCalories = mealService.totalMealsCalories(mealService.mealsList(userMealListRequest.getMealsIds()));
        modelAndView.addObject("user",userService.getById(authenticationDetails.getId()));
        modelAndView.addObject("allMeals", allMeals);
        modelAndView.addObject("mealsList", mealService.mealsList(userMealListRequest.getMealsIds()));
        modelAndView.addObject("totalCalories",totalCalories);
        return modelAndView;
    }


    @PostMapping("/chooseMeal")
    public String postMealToUser(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @RequestParam UUID mealId) {
        mealTrackingService.addMealToUser(authenticationDetails.getId(), mealId);
        return "redirect:/did_you_eat_enough_today";
    }
}