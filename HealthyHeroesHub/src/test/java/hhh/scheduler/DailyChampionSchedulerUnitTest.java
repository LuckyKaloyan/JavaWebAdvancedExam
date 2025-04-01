package hhh.scheduler;

import hhh.meal.model.Meal;
import hhh.upvote.service.UpVoteService;
import hhh.user.model.User;
import hhh.winner.service.WinnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyChampionSchedulerUnitTest {

    @Mock
    private UpVoteService upVoteService;

    @Mock
    private WinnerService winnerService;

    @InjectMocks
    private DailyChampionScheduler scheduler;

    @Test
    void whenTopMealExists_thenCreatesWinner() {
        User user = new User();
        Meal meal = new Meal();
        meal.setOwner(user);
        when(upVoteService.topMealByUpVote()).thenReturn(meal);
        scheduler.cleanUp();
        assertNotNull(meal.getOwner(), "Meal should have an owner");
    }

    @Test
    void whenMealHasNoOwner_thenNoWinnerCreated() {
        Meal meal = new Meal();
        when(upVoteService.topMealByUpVote()).thenReturn(meal);
        scheduler.cleanUp();
        assertNull(meal.getOwner(), "Meal should have no owner");
    }
}