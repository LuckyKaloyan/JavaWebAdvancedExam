package hhh.web;


import hhh.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExist(RedirectAttributes redirectAttributes){
           redirectAttributes.addFlashAttribute("messageUsernameAlreadyExist", "Username already exists!");
        return "redirect:/register";
    }
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public String handleEmailAlreadyInUse(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("messageEmailAlreadyInUse", "Email already in use!");
        return "redirect:/register";
    }
    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public String handlePasswordsDoNotMatch(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("messagePasswordsDoNotMatch", "The passwords do not match!");
        return "redirect:/register";
    }
    @ExceptionHandler(AlreadyUpVotedException.class)
    public String handleAlreadyUpVoted(AlreadyUpVotedException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String requestURL = request.getRequestURL().toString();
        String[] parts = requestURL.split("/");
        String mealId = parts[parts.length - 1];
        redirectAttributes.addFlashAttribute("alreadyVoted", e.getMessage());
        return "redirect:/meal_catalogs/meal/" + mealId;
    }
    @ExceptionHandler(AlreadyFavouriteException.class)
    public String handleAlreadyUpFavorite(AlreadyFavouriteException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String requestURL = request.getRequestURL().toString();
        String[] parts = requestURL.split("/");
        String mealId = parts[parts.length - 1];
        redirectAttributes.addFlashAttribute("alreadyFavourite", e.getMessage());
        return "redirect:/meal_catalogs/meal/" + mealId;
    }
}
