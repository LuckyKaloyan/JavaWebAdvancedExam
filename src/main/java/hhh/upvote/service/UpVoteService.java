package hhh.upvote.service;

import hhh.exception.AlreadyUpVotedException;
import hhh.exception.BadInputException;
import hhh.exception.NoUpVotesException;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.upvote.model.UpVote;
import hhh.upvote.repository.UpVoteRepository;
import hhh.user.model.User;
import hhh.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
        if (mealId == null) {
            throw new BadInputException("Meal ID cannot be null");
        }
        if (userId == null) {
            throw new BadInputException("User ID cannot be null");
        }

        Meal meal = mealService.getMealById(mealId);
        User user = userService.getById(userId);

        if (meal == null) {
            throw new BadInputException("Meal not found with ID: " + mealId);
        }
        if (user == null) {
            throw new BadInputException("User not found with ID: " + userId);
        }

        if (upVoteRepository.findByMealAndUser(meal, user).isPresent()) {
            throw new AlreadyUpVotedException("Only one upvote can be made!");
        }
        if (upVoteRepository.findByMealAndUser(meal, user).isPresent()) {
            throw new AlreadyUpVotedException("Only one upvote can be made!");
        } else {
            meal.getUpVotes().add(createUpVote(mealId, userId));
        }
    }

    @Transactional
    public void downVote(UUID mealId, UUID userId) {
        if (mealId == null) {
            throw new BadInputException("Meal ID cannot be null");
        }
        if (userId == null) {
            throw new BadInputException("User ID cannot be null");
        }

        Meal meal = mealService.getMealById(mealId);
        User user = userService.getById(userId);

        if (upVoteRepository.findByMealAndUser(meal, user).isPresent()) {
            UpVote upvote = upVoteRepository.findByMealAndUser(meal, user).get();
            upVoteRepository.delete(upvote);
            meal.getUpVotes().remove(upvote);
        } else {
            throw new BadInputException("The UpVote was not found!");
        }
    }

    public UpVote createUpVote(UUID mealId, UUID userId) {
        if (mealId == null) {
            throw new BadInputException("Meal ID cannot be null");
        }
        if (userId == null) {
            throw new BadInputException("User ID cannot be null");
        }

        User user = userService.getById(userId);
        Meal meal = mealService.getMealById(mealId);

        if (user == null) {
            throw new BadInputException("User not found with ID: " + userId);
        }
        if (meal == null) {
            throw new BadInputException("Meal not found with ID: " + mealId);
        }

        UpVote upVote = new UpVote();
        upVote.setUser(user);
        upVote.setMeal(meal);
        upVote.setDate(LocalDate.now());

        upVoteRepository.save(upVote);
        return upVote;
    }

    public boolean hasUserUpVoted(UUID mealId, UUID userId) {
        if (mealId == null) {
            throw new BadInputException("Meal ID cannot be null");
        }
        if (userId == null) {
            throw new BadInputException("User ID cannot be null");
        }

        Meal meal = mealService.getMealById(mealId);
        User user = userService.getById(userId);

        return upVoteRepository.findByMealAndUser(meal, user).isPresent();
    }

    public List<UpVote> getAllDateLastYear() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return upVoteRepository.findByDateAfter(oneYearAgo);
    }

    public List<UpVote> getAllDateLastMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        return upVoteRepository.findByDateAfter(oneMonthAgo);
    }

    public List<UpVote> getAllDateLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        return upVoteRepository.findByDateAfter(oneWeekAgo);
    }

    public List<UpVote> getAllDateLast24hours() {
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
        return upVoteRepository.findByDateAfter(oneDayAgo);
    }

    public List<UpVote> getAll() {
        return upVoteRepository.findAll();
    }

    @Transactional
    public void cleanUp() {
        upVoteRepository.deleteAllInBatch();
    }

    public Meal topMealByUpVote() {
        if (upVoteRepository.findMealWithMostUpVotes().isEmpty()) {
            throw new NoUpVotesException("There are no UpVotes yet... probably!");
        } else {
            return upVoteRepository.findMealWithMostUpVotes().get();
        }
    }
}
