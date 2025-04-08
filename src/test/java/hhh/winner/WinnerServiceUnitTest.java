package hhh.winner;
import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.winner.model.Winner;
import hhh.winner.repository.WinnerRepository;
import hhh.winner.service.WinnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WinnerServiceUnitTest {

    @Mock
    private WinnerRepository winnerRepository;
    @Mock
    private UserService userService;
    private WinnerService winnerService;
    private User user;
    private Meal meal;
    private Winner winner;

    @BeforeEach
    void setUp() {
        winnerService = new WinnerService(winnerRepository, userService);

        user = new User();
        meal = new Meal();
        winner = Winner.builder()
                .user(user)
                .meal(meal)
                .build();
    }

    @Test
    void newWinnerThrowWhenUserIsNull() {
        BadInputException exception = assertThrows(BadInputException.class, () -> winnerService.newWinner(null, meal));
        assertEquals("User ID cannot be null", exception.getMessage());
        verifyNoInteractions(winnerRepository);
    }

    @Test
    void newWinnerThrowWhenMealIsNull() {
        BadInputException exception = assertThrows(BadInputException.class, () -> winnerService.newWinner(user, null));
        assertEquals("Meal cannot be null", exception.getMessage());
        verifyNoInteractions(winnerRepository);
    }

    @Test
    void getTheWinnerReturnNullWhenNoWinnerExists() {
        when(winnerRepository.findAll()).thenReturn(Collections.emptyList());
        Winner result = winnerService.getTheWinner();
        assertNull(result);
        verify(winnerRepository, times(1)).findAll();
        verify(winnerRepository, never()).findFirstBy();
    }

    @Test
    void getTheWinnerReturnWinnerWhenExists() {
        when(winnerRepository.findAll()).thenReturn(Collections.singletonList(winner));
        when(winnerRepository.findFirstBy()).thenReturn(Optional.of(winner));
        Winner result = winnerService.getTheWinner();
        assertEquals(winner, result);
        verify(winnerRepository, times(1)).findAll();
        verify(winnerRepository, times(1)).findFirstBy();
    }
}