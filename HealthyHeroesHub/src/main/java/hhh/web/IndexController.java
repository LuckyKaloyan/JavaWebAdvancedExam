package hhh.web;
import hhh.meal.service.MealService;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.LoginRequest;
import hhh.web.dto.RegisterRequest;
import hhh.winner.service.WinnerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {


    private final UserService userService;
    private final MealCatalogService mealCatalogService;
    private final MealService mealService;
    private final WinnerService winnerService;



    @Autowired
    public IndexController(UserService userService, MealCatalogService mealCatalogService, MealService mealService, WinnerService winnerService) {
        this.userService = userService;
        this.mealCatalogService = mealCatalogService;
        this.mealService = mealService;
        this.winnerService = winnerService;
    }


    @GetMapping
    public ModelAndView getIndex(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAuthenticated", auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal()));
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegister(RegisterRequest registerRequest){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAuthenticated", auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken));
        modelAndView.addObject("registerRequest", registerRequest);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView postRegister(@Valid RegisterRequest registerRequest, BindingResult result) {

        if(result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("register");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            modelAndView.addObject("isAuthenticated", auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken));
            modelAndView.addObject("registerRequest", registerRequest);
            return modelAndView;
        }

        userService.register(registerRequest);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/login")
    public ModelAndView getLogin(LoginRequest loginRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAuthenticated",
                auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal()));

        modelAndView.addObject("loginRequest", loginRequest);
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getHome(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("allCatalogs", mealCatalogService.getAllMealCatalogs());
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        session.invalidate();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/info")
    public ModelAndView getContact(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("info");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAuthenticated", auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal()));
        return modelAndView;
    }
    @GetMapping("/favourite_meals")
    public ModelAndView getFavouriteMeals(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("favourite_meals");
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/leaderboard")
    public ModelAndView getLeaderboard(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leaderboard");
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("top20meals", mealService.getTop20Meals());
        return modelAndView;
    }

    @GetMapping("/winner")
    public ModelAndView getWinner(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("winner", winnerService.getTheWinner());
        modelAndView.setViewName("winner");
        return modelAndView;
    }

}