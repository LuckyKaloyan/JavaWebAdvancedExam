package hhh.scheduler;

import hhh.meal.model.Meal;
import hhh.upvote.service.UpVoteService;
import hhh.user.model.User;
import hhh.winner.service.WinnerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyChampionScheduler {

    private final UpVoteService upVoteService;
    private final WinnerService winnerService;

    @Autowired
    public DailyChampionScheduler(UpVoteService upVoteService, WinnerService winnerService) {
        this.upVoteService = upVoteService;
        this.winnerService = winnerService;
    }

    @Transactional
    @Scheduled(cron = "1 1 4 * * *")
    public void pickingTheWinner() {
        Meal meal = upVoteService.topMealByUpVote();
        User user = meal.getOwner();
        winnerService.newWinner(user, meal);
    }

    @Scheduled(cron = "6 3 3 * * *")
    public void cleanUp() {
        upVoteService.cleanUp();
    }
}
