package hhh.upvote;
import hhh.exception.AlreadyUpVotedException;
import hhh.exception.BadInputException;
import hhh.exception.NoUpVotesException;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.upvote.model.UpVote;
import hhh.upvote.repository.UpVoteRepository;
import hhh.upvote.service.UpVoteService;
import hhh.user.model.User;
import hhh.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpVoteServiceUnitTest {

    @Mock
    private UpVoteRepository upVoteRepository;

    @Mock
    private UserService userService;

    @Mock
    private MealService mealService;

    private UpVoteService upVoteService;

    private UUID mealId;
    private UUID userId;
    private Meal meal;
    private User user;
    private UpVote upVote;

    @BeforeEach
    void setUp() {
        upVoteService = new UpVoteService(upVoteRepository, userService, mealService);
        mealId = UUID.randomUUID();
        userId = UUID.randomUUID();
        meal = new Meal();
        meal.setId(mealId);
        user = new User();
        user.setId(userId);
        upVote = UpVote.builder()
                .meal(meal)
                .user(user)
                .date(LocalDate.now())
                .build();
    }

    @Test
    void upVote_shouldThrowWhenAlreadyUpVoted() {
        when(mealService.getMealById(mealId)).thenReturn(meal);
        when(userService.getById(userId)).thenReturn(user);
        when(upVoteRepository.findByMealAndUser(meal, user)).thenReturn(Optional.of(upVote));
        assertThrows(AlreadyUpVotedException.class, () -> upVoteService.upVote(mealId, userId));
    }

    @Test
    void hasUserUpVoted_shouldReturnTrueWhenUpVoteExists() {
        when(mealService.getMealById(mealId)).thenReturn(meal);
        when(userService.getById(userId)).thenReturn(user);
        when(upVoteRepository.findByMealAndUser(meal, user)).thenReturn(Optional.of(upVote));
        assertTrue(upVoteService.hasUserUpVoted(mealId, userId));
    }

    @Test
    void topMealByUpVote_shouldReturnMealWithMostUpVotes() {
        when(upVoteRepository.findMealWithMostUpVotes()).thenReturn(Optional.of(meal));
        Meal result = upVoteService.topMealByUpVote();
        assertEquals(meal, result);
    }

    @Test
    void upVote_shouldThrowWhenMealIdIsNull() {
        assertThrows(BadInputException.class, () -> upVoteService.upVote(null, userId));
    }

    @Test
    void upVote_shouldThrowWhenUserIdIsNull() {
        assertThrows(BadInputException.class, () -> upVoteService.upVote(mealId, null));
    }

    @Test
    void getAllDateLastYear_shouldReturnRecentUpVotes() {
        List<UpVote> expected = Collections.singletonList(upVote);
        when(upVoteRepository.findByDateAfter(any(LocalDate.class))).thenReturn(expected);
        List<UpVote> result = upVoteService.getAllDateLastYear();
        assertEquals(expected, result);
    }

    @Test
    void topMealByUpVote_shouldThrowWhenNoUpVotesExist() {
        when(upVoteRepository.findMealWithMostUpVotes()).thenReturn(Optional.empty());
        assertThrows(NoUpVotesException.class, () -> upVoteService.topMealByUpVote());
    }

    @Test
    void cleanUp_shouldDeleteAllUpVotes() {
        upVoteService.cleanUp();
        verify(upVoteRepository, times(1)).deleteAllInBatch();
    }
}