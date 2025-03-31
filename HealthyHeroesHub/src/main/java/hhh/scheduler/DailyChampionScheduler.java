package hhh.scheduler;
import hhh.meal.model.Meal;
import hhh.upvote.service.UpVoteService;
import hhh.user.model.User;
import hhh.winner.service.WinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DailyChampionScheduler {

    private final UpVoteService upVoteService;
    private final WinnerService winnerService;



    @Autowired
    public DailyChampionScheduler(UpVoteService upVoteService, WinnerService winnerService) {
        this.upVoteService = upVoteService;
        this.winnerService = winnerService;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUp() {
        Meal meal = upVoteService.topMealByUpVote();
        User user = meal.getOwner();
        winnerService.newWinner(user,meal);
        upVoteService.cleanUp();
    }
}
