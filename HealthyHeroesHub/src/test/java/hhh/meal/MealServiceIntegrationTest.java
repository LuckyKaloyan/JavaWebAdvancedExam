package hhh.meal;
import hhh.exception.AlreadyFavouriteException;
import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.meal.repository.MealRepository;
import hhh.meal.service.MealService;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.model.MealCatalogType;
import hhh.mealcatalog.repository.MealCatalogRepository;
import hhh.user.model.User;
import hhh.user.model.UserRole;
import hhh.user.repository.UserRepository;
import hhh.web.dto.MealRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MealServiceIntegrationTest {

    @Autowired
    private MealService mealService;
    @Autowired
    private MealCatalogRepository mealCatalogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MealRepository mealRepository;

    @Test
    void testAddMealToCatalogSuccessfully() {
        User user = User.builder()
                .email("test@example.com")
                .registrationDate(LocalDate.now())
                .role(UserRole.USER)
                .username("testuser")
                .password("validpass").build();
        userRepository.saveAndFlush(user);

        MealCatalog mealCatalog = MealCatalog.builder()
                .type(MealCatalogType.VEGAN)
                .meals(new ArrayList<>())
                .owner(user)
                .description("Test catalog")
                .name("Test Catalog")
                .addedOn(LocalDate.now())
                .build();
        mealCatalogRepository.saveAndFlush(mealCatalog);

        MealRequest mealRequest = new MealRequest(
                "Healthy Salad",
                "Fresh vegetables salad",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(20),
                "salad.jpg");

        mealService.addMeal(mealRequest, mealCatalog);

        List<Meal> mealsInCatalog = mealRepository.findByMealCatalog(mealCatalog);
        assertEquals(1, mealsInCatalog.size());

        Meal savedMeal = mealsInCatalog.get(0);
        assertNotNull(savedMeal.getId());
        assertEquals("Healthy Salad", savedMeal.getName());
        assertEquals("Fresh vegetables salad", savedMeal.getDescription());
        assertEquals(0, BigDecimal.valueOf(5).compareTo(savedMeal.getProteins()));
        assertEquals(0, BigDecimal.valueOf(10).compareTo(savedMeal.getCarbs()));
        assertEquals(0, BigDecimal.valueOf(20).compareTo(savedMeal.getFats()));
        assertEquals("salad.jpg", savedMeal.getPicture());
        assertEquals(user.getId(), savedMeal.getOwner().getId());
        assertEquals(mealCatalog.getId(), savedMeal.getMealCatalog().getId());
        BigDecimal expectedCalories = BigDecimal.valueOf(220);
        assertEquals(0, expectedCalories.compareTo(savedMeal.getTotalCalories()));
    }
    @Test
    void testAddMealWithInvalidInputs() {
        User user = User.builder()
                .email("test@example.com")
                .registrationDate(LocalDate.now())
                .role(UserRole.USER)
                .username("testuser")
                .password("validpass").build();
        userRepository.saveAndFlush(user);

        MealCatalog mealCatalog = MealCatalog.builder()
                .type(MealCatalogType.VEGAN)
                .meals(new ArrayList<>())
                .owner(user)
                .description("Test catalog")
                .name("Test Catalog")
                .addedOn(LocalDate.now())
                .build();
        mealCatalogRepository.saveAndFlush(mealCatalog);

        assertThrows(BadInputException.class, () -> mealService.addMeal(null, mealCatalog));
        MealRequest emptyNameRequest = new MealRequest(
                "",
                "Description",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(20),
                "image.jpg");
        assertThrows(BadInputException.class, () -> mealService.addMeal(emptyNameRequest, mealCatalog));

        MealRequest negativeCarbsRequest = new MealRequest(
                "Meal",
                "Description",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(20),
                "image.jpg");
        assertThrows(BadInputException.class, () -> mealService.addMeal(negativeCarbsRequest, mealCatalog));

        MealRequest nullProteinsRequest = new MealRequest(
                "Meal",
                "Description",
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(20),
                "image.jpg");
        assertThrows(BadInputException.class, () -> mealService.addMeal(nullProteinsRequest, mealCatalog));
    }

    @Test
    void testGetMealById() {
        User user = User.builder()
                .email("test@example.com")
                .registrationDate(LocalDate.now())
                .role(UserRole.USER)
                .username("testuser")
                .password("validpass").build();
        userRepository.saveAndFlush(user);

        MealCatalog mealCatalog = MealCatalog.builder()
                .type(MealCatalogType.VEGAN)
                .meals(new ArrayList<>())
                .owner(user)
                .description("Test catalog")
                .name("Test Catalog")
                .addedOn(LocalDate.now())
                .build();
        mealCatalogRepository.saveAndFlush(mealCatalog);

        Meal meal = Meal.builder()
                .name("Test Meal")
                .description("Test Description")
                .proteins(BigDecimal.valueOf(10))
                .carbs(BigDecimal.valueOf(20))
                .fats(BigDecimal.valueOf(5))
                .totalCalories(BigDecimal.valueOf(180))
                .picture("test.jpg")
                .addedOn(LocalDate.now())
                .mealCatalog(mealCatalog)
                .owner(user)
                .build();
        mealRepository.saveAndFlush(meal);

        Meal retrievedMeal = mealService.getMealById(meal.getId());
        assertEquals(meal.getId(), retrievedMeal.getId());
        assertEquals("Test Meal", retrievedMeal.getName());
        assertThrows(BadInputException.class, () -> mealService.getMealById(UUID.randomUUID()));
    }

}