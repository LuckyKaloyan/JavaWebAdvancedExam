package hhh.meal;

import hhh.exception.BadInputException;
import hhh.meal.model.FavouriteMeal;
import hhh.meal.model.Meal;
import hhh.meal.repository.FavouriteMealRepository;
import hhh.meal.repository.MealRepository;
import hhh.meal.service.MealService;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.MealRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceUnitTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private FavouriteMealRepository favouriteMealRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MealService mealService;

    @Test
    void addMealThrowExceptionWhenRequestIsNull() {
        MealCatalog catalog = new MealCatalog();
        assertThrows(BadInputException.class, () -> mealService.addMeal(null, catalog));
    }

    @Test
    void addMealThrowExceptionWhenCatalogIsNull() {
        MealRequest request = new MealRequest();
        request.setName("Test");
        assertThrows(BadInputException.class, () -> mealService.addMeal(request, null));
    }

    @Test
    void addMealThrowExceptionWhenNameIsBlank() {
        MealRequest request = new MealRequest();
        request.setName("");
        MealCatalog catalog = new MealCatalog();
        assertThrows(BadInputException.class, () -> mealService.addMeal(request, catalog));
    }

    @Test
    void addMealThrowExceptionWhenCarbsIsNegative() {
        MealRequest request = new MealRequest();
        request.setName("Test");
        request.setCarbs(BigDecimal.valueOf(-1));
        MealCatalog catalog = new MealCatalog();
        assertThrows(BadInputException.class, () -> mealService.addMeal(request, catalog));
    }


    @Test
    void getMealByIdThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(mealRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> mealService.getMealById(id));
    }

    @Test
    void getMealByIdReturnMeal() {
        UUID id = UUID.randomUUID();
        Meal meal = new Meal();
        when(mealRepository.findById(id)).thenReturn(Optional.of(meal));
        assertEquals(meal, mealService.getMealById(id));
    }



    @Test
    void createFavouriteMealThrowExceptionWhenUserIsNull() {
        Meal meal = new Meal();
        assertThrows(BadInputException.class, () -> mealService.createFavouriteMeal(null, meal));
    }

    @Test
    void createFavouriteMealThrowExceptionWhenMealIsNull() {
        User user = new User();
        assertThrows(BadInputException.class, () -> mealService.createFavouriteMeal(user, null));
    }

    @Test
    void getAllMealsReturnAllMeals() {
        List<Meal> meals = new ArrayList<>();
        when(mealRepository.findAll()).thenReturn(meals);
        assertEquals(meals, mealService.getAllMeals());
    }

    @Test
    void getAllAddedLastMonthReturnRecentMeals() {
        LocalDate date = LocalDate.now().minusMonths(1);
        List<Meal> meals = new ArrayList<>();
        when(mealRepository.findByAddedOnAfter(date)).thenReturn(meals);
        assertEquals(meals, mealService.getAllAddedLastMonth());
    }

    @Test
    void mealsListReturnOrderedMeals() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Meal meal1 = new Meal();
        meal1.setId(id1);
        Meal meal2 = new Meal();
        meal2.setId(id2);
        when(mealRepository.findAllById(any())).thenReturn(Arrays.asList(meal1, meal2));
        List<Meal> result = mealService.mealsList(Arrays.asList(id1, id2));
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0).getId());
    }

    @Test
    void totalMealsCaloriesReturnCorrectSum() {
        Meal meal1 = new Meal();
        meal1.setTotalCalories(BigDecimal.valueOf(100));
        Meal meal2 = new Meal();
        meal2.setTotalCalories(BigDecimal.valueOf(200));
        double result = mealService.totalMealsCalories(Arrays.asList(meal1, meal2));
        assertEquals(300.0, result);
    }

    @Test
    void createFavouriteMealWithValidInputReturnsFavouriteMeal() {
        User user = new User();
        Meal meal = new Meal();
        FavouriteMeal expectedFavouriteMeal = FavouriteMeal.builder()
                .user(user)
                .meal(meal)
                .favouritedOn(LocalDate.now())
                .build();

        when(favouriteMealRepository.save(any(FavouriteMeal.class))).thenReturn(expectedFavouriteMeal);
        FavouriteMeal result = mealService.createFavouriteMeal(user, meal);
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(meal, result.getMeal());
        assertEquals(LocalDate.now(), result.getFavouritedOn());
    }

    @Test
    void createFavouriteMealWithNullUserThrowsBadInputException() {
        Meal meal = new Meal();
        assertThrows(BadInputException.class, () -> {mealService.createFavouriteMeal(null, meal);});
    }

    @Test
    void createFavouriteMealWithNullMealThrowsBadInputException() {
        User user = new User();
        assertThrows(BadInputException.class, () -> {mealService.createFavouriteMeal(user, null);});
    }

    @Test
    void deleteFavouriteMealWithValidIdDeletesFavourite() {
        UUID favouriteMealId = UUID.randomUUID();
        when(favouriteMealRepository.findById(favouriteMealId)).thenReturn(Optional.of(new FavouriteMeal()));
        mealService.deleteFavouriteMeal(favouriteMealId);
        verify(favouriteMealRepository, times(1)).deleteById(favouriteMealId);
    }

    @Test
    void deleteFavouriteMealWithInvalidIdThrowsBadInputException() {
        UUID invalidId = UUID.randomUUID();
        when(favouriteMealRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> {mealService.deleteFavouriteMeal(invalidId);});
    }
    @Test
    void unFavouriteMealWithNullUserThrowsBadInputException() {
        UUID userId = UUID.randomUUID();
        UUID mealId = UUID.randomUUID();
        when(userService.getById(userId)).thenReturn(null);
        assertThrows(BadInputException.class, () -> {mealService.unFavouriteMeal(mealId, userId);});
    }
    @Test
    void isFavouriteWithNullUserThrowsBadInputException() {
        UUID userId = UUID.randomUUID();
        UUID mealId = UUID.randomUUID();
        when(userService.getById(userId)).thenReturn(null);
        assertThrows(BadInputException.class, () -> {mealService.isFavourite(mealId, userId);});
    }
}