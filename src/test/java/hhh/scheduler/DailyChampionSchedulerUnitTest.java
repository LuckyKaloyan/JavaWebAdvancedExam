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


}