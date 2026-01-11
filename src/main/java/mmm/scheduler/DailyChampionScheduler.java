package mmm.scheduler;

import mmm.meal.model.Meal;
import mmm.upvote.service.UpVoteService;
import mmm.user.model.User;
import mmm.winner.service.WinnerService;
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
