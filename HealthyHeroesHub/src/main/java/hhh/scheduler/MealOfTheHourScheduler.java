package hhh.scheduler;

import hhh.event.MealOfTheDayEvent;
import hhh.meal.model.Meal;
import hhh.meal.model.MealOfTheHour;
import hhh.meal.service.MealService;
import hhh.upvote.service.UpVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MealOfTheHourScheduler {

    private final MealService mealService;
    private final UpVoteService upVoteService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public MealOfTheHourScheduler(MealService mealService, UpVoteService upVoteService, ApplicationEventPublisher eventPublisher) {
        this.mealService = mealService;
        this.upVoteService = upVoteService;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 3600000)
    public void publishMealOfTheHour() {
        MealOfTheHour mealOfTheHour = mealService.getMealOfTheHour();
        Meal topMeal = mealService.getTopMeal();

        if (mealOfTheHour.getMealId() != topMeal.getId()) {
            mealService.mealOfTheHour();
            upVoteService.cleanUp();

            Meal meal = mealService.getMealById(mealOfTheHour.getMealId());
            MealOfTheDayEvent event = MealOfTheDayEvent.builder()
                    .upVotes(meal.getUpVotes().size())
                    .name(meal.getName())
                    .ownersName(meal.getOwner().getUsername())
                    .description(meal.getDescription())
                    .build();
            eventPublisher.publishEvent(event);
        }
    }
}
