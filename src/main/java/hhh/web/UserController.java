package hhh.web;
import hhh.security.AuthenticationDetails;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.EditProfileRequest;
import hhh.web.mapper.Mapper;
import hhh.winner.service.WinnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final WinnerService winnerService;

    @Autowired
    public UserController(UserService userService, WinnerService winnerService) {
        this.userService = userService;
        this.winnerService = winnerService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getProfile(@PathVariable UUID id) {

        User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", Mapper.toEditProfileRequest(user));
        return modelAndView;
    }
    @PutMapping("/{id}/profile")
    public ModelAndView editProfile(@PathVariable UUID id, @Valid EditProfileRequest editProfileRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            User user = userService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.setViewName("edit-profile");
            modelAndView.addObject("editProfileRequest", editProfileRequest);
            return modelAndView;
        }
        userService.editUser(id, editProfileRequest);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/winner")
    public ModelAndView getWinner(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("winner", winnerService.getTheWinner());
        modelAndView.setViewName("winner");
        return modelAndView;
    }
}