package hhh.web;


import ch.qos.logback.core.model.Model;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin_panel")
public class AdminPanelController {

    private final UserService userService;

    @Autowired
    public AdminPanelController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView adminPanel(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin_panel");
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);

        return modelAndView;
    }





}
