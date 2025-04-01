package hhh.meal;

import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.meal.repository.MealRepository;
import hhh.meal.service.MealService;
import hhh.meal_tracking.client.MealTrackingClient;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
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
    private MealTrackingClient mealTrackingClient;

    @InjectMocks
    private MealService mealService;

    @Test
    void addMeal_shouldThrowExceptionWhenRequestIsNull() {
        MealCatalog catalog = new MealCatalog();
        assertThrows(BadInputException.class, () -> mealService.addMeal(null, catalog));
    }

    @Test
    void addMeal_shouldThrowExceptionWhenCatalogIsNull() {
        MealRequest request = new MealRequest();
        request.setName("Test");
        assertThrows(BadInputException.class, () -> mealService.addMeal(request, null));
    }

    @Test
    void addMeal_shouldThrowExceptionWhenNameIsBlank() {
        MealRequest request = new MealRequest();
        request.setName("");
        MealCatalog catalog = new MealCatalog();
        assertThrows(BadInputException.class, () -> mealService.addMeal(request, catalog));
    }

    @Test
    void addMeal_shouldThrowExceptionWhenCarbsIsNegative() {
        MealRequest request = new MealRequest();
        request.setName("Test");
        request.setCarbs(BigDecimal.valueOf(-1));
        MealCatalog catalog = new MealCatalog();
        assertThrows(BadInputException.class, () -> mealService.addMeal(request, catalog));
    }


    @Test
    void getMealById_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(mealRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> mealService.getMealById(id));
    }

    @Test
    void getMealById_shouldReturnMeal() {
        UUID id = UUID.randomUUID();
        Meal meal = new Meal();
        when(mealRepository.findById(id)).thenReturn(Optional.of(meal));
        assertEquals(meal, mealService.getMealById(id));
    }

    @Test
    void deleteMealById_shouldCallTrackingClient() {
        UUID id = UUID.randomUUID();
        when(mealTrackingClient.removeMealFromAllUsers(any())).thenReturn(ResponseEntity.ok().build());
        mealService.deleteMealById(id);
        verify(mealTrackingClient).removeMealFromAllUsers(id);
        verify(mealRepository).deleteById(id);
    }

    @Test
    void createFavouriteMeal_shouldThrowExceptionWhenUserIsNull() {
        Meal meal = new Meal();
        assertThrows(BadInputException.class, () -> mealService.createFavouriteMeal(null, meal));
    }

    @Test
    void createFavouriteMeal_shouldThrowExceptionWhenMealIsNull() {
        User user = new User();
        assertThrows(BadInputException.class, () -> mealService.createFavouriteMeal(user, null));
    }

    @Test
    void getAllMeals_shouldReturnAllMeals() {
        List<Meal> meals = new ArrayList<>();
        when(mealRepository.findAll()).thenReturn(meals);
        assertEquals(meals, mealService.getAllMeals());
    }

    @Test
    void getAllAddedLastMonth_shouldReturnRecentMeals() {
        LocalDate date = LocalDate.now().minusMonths(1);
        List<Meal> meals = new ArrayList<>();
        when(mealRepository.findByAddedOnAfter(date)).thenReturn(meals);
        assertEquals(meals, mealService.getAllAddedLastMonth());
    }

    @Test
    void mealsList_shouldReturnOrderedMeals() {
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
    void totalMealsCalories_shouldReturnCorrectSum() {
        Meal meal1 = new Meal();
        meal1.setTotalCalories(BigDecimal.valueOf(100));
        Meal meal2 = new Meal();
        meal2.setTotalCalories(BigDecimal.valueOf(200));
        double result = mealService.totalMealsCalories(Arrays.asList(meal1, meal2));
        assertEquals(300.0, result);
    }
}