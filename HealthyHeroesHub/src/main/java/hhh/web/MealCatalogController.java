package hhh.web;
import hhh.comment.service.CommentService;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.security.AuthenticationDetails;
import hhh.upvote.service.UpVoteService;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.CommentRequest;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.MealCatalogRequest;
import hhh.web.dto.MealRequest;
import hhh.web.mapper.Mapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/meal_catalogs")
public class MealCatalogController {

    private final MealCatalogService mealCatalogService;
    private final UserService userService;
    private final MealService mealService;
    private final UpVoteService upVoteService;
    private final CommentService commentService;


    @Autowired
    public MealCatalogController(MealCatalogService mealCatalogService, UserService userService, MealService mealService, UpVoteService upVoteService, CommentService commentService) {
        this.mealCatalogService = mealCatalogService;
        this.userService = userService;
        this.mealService = mealService;
        this.upVoteService = upVoteService;
        this.commentService = commentService;

    }

    @GetMapping("/create_new_catalog")
    public ModelAndView createNewCatalog(MealCatalogRequest mealCatalogRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create_new_catalog");
        modelAndView.addObject("mealCatalogRequest", mealCatalogRequest);
        return modelAndView;
    }

    @PostMapping("/create_new_catalog")
    public ModelAndView postNewCatalog(@Valid MealCatalogRequest mealCatalogRequest, BindingResult result, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if(result.hasErrors()) {
            return new ModelAndView("create_new_catalog");
        }
        User user = userService.getById(authenticationDetails.getId());
        mealCatalogService.createMealCatalog(mealCatalogRequest, user);
        return new ModelAndView("redirect:/home");
    }


    @GetMapping("/{id}")
    public ModelAndView getMealCatalog(@PathVariable UUID id,  @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();
        MealCatalog mealCatalog = mealCatalogService.getMealCatalogById(id);
        modelAndView.addObject("mealCatalog", mealCatalog);
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("meal_catalog");
        return modelAndView;
    }

    @GetMapping("{id}/edit")
    public ModelAndView getEditMealCatalog(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit_catalog");
        MealCatalog mealCatalog = mealCatalogService.getMealCatalogById(id);
        modelAndView.addObject("mealCatalog", mealCatalog);
        modelAndView.addObject("editCatalogRequest", Mapper.toEditCatalogRequest(mealCatalog));

        return modelAndView;

    }
    @PatchMapping("{id}/edit")
    public ModelAndView patchMealCatalog(@PathVariable UUID id, @Valid EditCatalogRequest editCatalogRequest, BindingResult bindingResult) {
       if(bindingResult.hasErrors()) {
           ModelAndView modelAndView = new ModelAndView();
           modelAndView.setViewName("edit_catalog");
           MealCatalog mealCatalog = mealCatalogService.getMealCatalogById(id);
           modelAndView.addObject("mealCatalog", mealCatalog);
           modelAndView.addObject("editCatalogRequest", editCatalogRequest);
            return modelAndView;
       }
        mealCatalogService.editMealCatalog(id,editCatalogRequest);
        return new ModelAndView("redirect:/home");

    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteMealCatalog(@PathVariable UUID id) {
        mealCatalogService.deleteCatalog(id);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/{id}/add_meal")
    public ModelAndView addNewMeal(@PathVariable UUID id, MealRequest mealRequest) {

        return addMeal(id, mealRequest);
    }
    @PostMapping("/{id}/add_meal")
    public ModelAndView postNewMeal (@PathVariable UUID id, @Valid MealRequest mealRequest, BindingResult result, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        if(result.hasErrors()) {
            return addMeal(id, mealRequest);
        }
        MealCatalog mealCatalog = mealCatalogService.getMealCatalogById(id);
        mealService.addMeal(mealRequest,mealCatalog);
        return new ModelAndView("redirect:/meal_catalogs/"+id);
    }

    private ModelAndView addMeal(@PathVariable UUID id, @Valid MealRequest mealRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add_meal");
        MealCatalog mealCatalog = mealCatalogService.getMealCatalogById(id);
        modelAndView.addObject("mealCatalog", mealCatalog);
        modelAndView.addObject("mealRequest", mealRequest);
        modelAndView.addObject("mealCatalog", mealCatalogService.getMealCatalogById(id));
        return modelAndView;
    }

    @GetMapping("/meal/{id}")
    public ModelAndView getMeal(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, CommentRequest commentRequest) {
        return getModelAndView(id, authenticationDetails, commentRequest);
    }
    @DeleteMapping("{id1}/meal/delete/{id2}")
    public String deleteMeal(@PathVariable UUID id1, @PathVariable UUID id2) {
        mealService.deleteMealById(id2);
        return "redirect:/meal_catalogs/" + id1;
    }

    @PostMapping("/meals/add_to_favourite/{id}")
    public ModelAndView addFavourite(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        mealService.addFavouriteMeal(id,authenticationDetails.getId());
        return new ModelAndView("redirect:/meal_catalogs/meal/"+id);
    }

    @PostMapping("meals/up_vote/{id}")
    public ModelAndView upVote(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        upVoteService.upVote(id,authenticationDetails.getId());
        return new ModelAndView("redirect:/meal_catalogs/meal/"+id);
    }
    @DeleteMapping("meals/up_vote/{id}")
    public ModelAndView downVote(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        upVoteService.downVote(id,authenticationDetails.getId());
        return new ModelAndView("redirect:/meal_catalogs/meal/"+id);
    }
    @DeleteMapping("/meals/unfavourite/{id}")
    public ModelAndView unFavourite(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        mealService.unFavouriteMeal(id,authenticationDetails.getId());
        return new ModelAndView("redirect:/meal_catalogs/meal/"+id);
    }
    @DeleteMapping("/meals/remove_from_favourite/{id}")
    public ModelAndView removeFavourite(@PathVariable UUID id) {
        mealService.deleteFavouriteMeal(id);
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/meal/{id}/add_comment")
    public ModelAndView postComment(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid CommentRequest commentRequest, BindingResult result){
        if(result.hasErrors()) {
            return getModelAndView(id, authenticationDetails, commentRequest);
        }else {
            commentService.createComment(commentRequest.getText(),authenticationDetails.getId(),id);
            return new ModelAndView("redirect:/meal_catalogs/meal/"+id);
        }
    }

    @DeleteMapping("/meal/{id}/delete_comment/{id2}")
    public ModelAndView deleteComment(@PathVariable UUID id, @PathVariable UUID id2) {
        commentService.deleteCommentById(id2);
        return new ModelAndView("redirect:/meal_catalogs/meal/"+id);
    }

    private ModelAndView getModelAndView(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, @Valid CommentRequest commentRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("meal");
        Meal meal = mealService.getMealById(id);
        User user = userService.getById(authenticationDetails.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("meal", meal);
        modelAndView.addObject("commentRequest", commentRequest);
        modelAndView.addObject("hasVoted", upVoteService.hasUserUpVoted(id,authenticationDetails.getId()));
        modelAndView.addObject("isFavourite", mealService.isFavourite(id,authenticationDetails.getId()));
        return modelAndView;
    }


}