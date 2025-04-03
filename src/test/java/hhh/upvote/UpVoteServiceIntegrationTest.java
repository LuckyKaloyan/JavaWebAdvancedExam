package hhh.upvote;
import hhh.exception.AlreadyUpVotedException;
import hhh.exception.BadInputException;
import hhh.exception.NoUpVotesException;
import hhh.meal.model.Meal;
import hhh.meal.repository.MealRepository;
import hhh.meal.service.MealService;
import hhh.upvote.model.UpVote;
import hhh.upvote.repository.UpVoteRepository;
import hhh.upvote.service.UpVoteService;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UpVoteServiceIntegrationTest {

    @Autowired
    private UpVoteService upVoteService;
    @Autowired
    private UpVoteRepository upVoteRepository;

    @Autowired
    private MealService mealService;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testUpVoteSuccessfully() {
        User user = createTestUser();
        Meal meal = createTestMeal(user);
        upVoteService.upVote(meal.getId(), user.getId());
        List<UpVote> upVotes = upVoteRepository.findAll();
        assertEquals(1, upVotes.size());
        UpVote upVote = upVotes.get(0);
        assertEquals(user.getId(), upVote.getUser().getId());
        assertEquals(meal.getId(), upVote.getMeal().getId());
        assertEquals(LocalDate.now(), upVote.getDate());
        Meal updatedMeal = mealService.getMealById(meal.getId());
        assertEquals(1, updatedMeal.getUpVotes().size());
    }

    @Test
    void testUpVoteWithInvalidInputs() {
        assertThrows(BadInputException.class, () -> upVoteService.upVote(null, UUID.randomUUID()));
        assertThrows(BadInputException.class, () -> upVoteService.upVote(UUID.randomUUID(), null));
        User user = createTestUser();
        assertThrows(BadInputException.class, () -> upVoteService.upVote(UUID.randomUUID(), user.getId()));
        Meal meal = createTestMeal(user);
        assertThrows(BadInputException.class, () -> upVoteService.upVote(meal.getId(), UUID.randomUUID()));
    }

    @Test
    void testDuplicateUpVote() {
        User user = createTestUser();
        Meal meal = createTestMeal(user);
        upVoteService.upVote(meal.getId(), user.getId());
        assertThrows(AlreadyUpVotedException.class, () -> upVoteService.upVote(meal.getId(), user.getId()));
    }
    @Test
    void testDownVoteInvalidInputs() {
        assertThrows(BadInputException.class, () -> upVoteService.downVote(null, UUID.randomUUID()));
        assertThrows(BadInputException.class, () -> upVoteService.downVote(UUID.randomUUID(), null));
    }

    @Test
    void testDownVoteNonExistingVote() {
        User user = createTestUser();
        Meal meal = createTestMeal(user);
        assertThrows(BadInputException.class, () -> upVoteService.downVote(meal.getId(), user.getId()));
    }

    @Test
    void testHasUserUpVoted() {
        User user = createTestUser();
        Meal meal = createTestMeal(user);
        assertFalse(upVoteService.hasUserUpVoted(meal.getId(), user.getId()));
        upVoteService.upVote(meal.getId(), user.getId());
        assertTrue(upVoteService.hasUserUpVoted(meal.getId(), user.getId()));
        upVoteService.downVote(meal.getId(), user.getId());
        assertFalse(upVoteService.hasUserUpVoted(meal.getId(), user.getId()));
    }

    @Test
    void testTimeBasedUpVoteRetrieval() {
        User user = createTestUser();
        Meal meal = createTestMeal(user);
        createTestUpVote(meal, user, LocalDate.now()); // today
        createTestUpVote(meal, user, LocalDate.now().minusDays(1)); // yesterday
        createTestUpVote(meal, user, LocalDate.now().minusWeeks(1)); // last week
        createTestUpVote(meal, user, LocalDate.now().minusMonths(1)); // last month
        createTestUpVote(meal, user, LocalDate.now().minusYears(1)); // last year
        createTestUpVote(meal, user, LocalDate.now().minusYears(2)); // older
        assertEquals(1, upVoteService.getAllDateLast24hours().size());
        assertEquals(2, upVoteService.getAllDateLastWeek().size());
        assertEquals(3, upVoteService.getAllDateLastMonth().size());
        assertEquals(4, upVoteService.getAllDateLastYear().size());
    }

    @Test
    void testTopMealByUpVote() {
        User user = createTestUser();
        Meal meal1 = createTestMeal(user);
        Meal meal2 = createTestMeal(user);
        assertThrows(NoUpVotesException.class, () -> upVoteService.topMealByUpVote());
        upVoteService.upVote(meal1.getId(), user.getId());
        assertEquals(meal1.getId(), upVoteService.topMealByUpVote().getId());
        User user2 = User.builder()
                .email("user2@example.com")
                .username("user2")
                .password("password")
                .role(UserRole.USER)
                .registrationDate(LocalDate.now())
                .build();
        userRepository.save(user2);
        upVoteService.upVote(meal2.getId(), user.getId());
        upVoteService.upVote(meal2.getId(), user2.getId());
        assertEquals(meal2.getId(), upVoteService.topMealByUpVote().getId());
    }

    @Test
    void testCleanUp() {
        User user = createTestUser();
        Meal meal = createTestMeal(user);
        upVoteService.upVote(meal.getId(), user.getId());
        assertEquals(1, upVoteRepository.count());
        upVoteService.cleanUp();
        assertEquals(0, upVoteRepository.count());
    }
    private User createTestUser() {
        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .role(UserRole.USER)
                .registrationDate(LocalDate.now())
                .build();
        return userRepository.save(user);
    }

    private Meal createTestMeal(User owner) {
        Meal meal = Meal.builder()
                .name("Test Meal")
                .description("Test Description")
                .proteins(BigDecimal.valueOf(10))
                .carbs(BigDecimal.valueOf(20))
                .fats(BigDecimal.valueOf(5))
                .totalCalories(BigDecimal.valueOf(180))
                .picture("test.jpg")
                .addedOn(LocalDate.now())
                .owner(owner)
                .build();
        return mealRepository.save(meal);
    }

    private void createTestUpVote(Meal meal, User user, LocalDate date) {
        UpVote upVote = UpVote.builder()
                .meal(meal)
                .user(user)
                .date(date)
                .build();
        upVoteRepository.save(upVote);
    }
}