package hhh.upvote.service;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.upvote.model.UpVote;
import hhh.upvote.repository.UpVoteRepository;
import hhh.user.model.User;
import hhh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UpVoteService {

    private final UpVoteRepository upVoteRepository;
    private final UserService userService;
    private final MealService mealService;

    @Autowired
    public UpVoteService(UpVoteRepository upVoteRepository, UserService userService, MealService mealService) {
        this.upVoteRepository = upVoteRepository;
        this.userService = userService;
        this.mealService = mealService;
    }

    public void upVote(UUID mealId, UUID userId) {

        Meal meal = mealService.getMealById(mealId);
        User user = userService.getById(userId);
        if(upVoteRepository.findByMealAndUser(meal,user).isPresent()){
            throw new RuntimeException("You cannot keep voting bruh! Only one upvote can be made!");
        }else  meal.getUpVotes().add(createUpVote(mealId, userId));

    }

    public UpVote createUpVote(UUID mealId, UUID userId) {
        User user = userService.getById(userId);
        Meal meal = mealService.getMealById(mealId);
        UpVote upVote = UpVote.builder()
                .user(user)
                .meal(meal)
                .date(LocalDate.now())
                .build();
        upVoteRepository.save(upVote);
        return upVote;
    }

}
