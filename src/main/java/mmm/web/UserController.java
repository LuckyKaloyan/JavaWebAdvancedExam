package mmm.web;
import mmm.security.AuthenticationDetails;
import mmm.user.model.User;
import mmm.user.service.UserService;
import mmm.web.dto.EditProfileRequest;
import mmm.web.mapper.Mapper;
import mmm.winner.service.WinnerService;
import jakarta.servlet.http.HttpSession;
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
    @GetMapping("/delete_profile")
    public ModelAndView getSelfDeleteUser(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("delete_profile");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @DeleteMapping("/{id}/deleted")
    public ModelAndView confirmDeleteUser(@PathVariable UUID id, HttpSession session) {
        userService.deleteUser(id);
        ModelAndView modelAndView = new ModelAndView();
        session.invalidate();
        modelAndView.setViewName("redirect:/");
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