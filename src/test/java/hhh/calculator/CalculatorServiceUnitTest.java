package hhh.calculator;

import hhh.exception.BadInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceUnitTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void calculateDailyCaloriesThrowExceptionForNegativeWeight() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(-1, 170, 30, Gender.MALE, Activity.MODERATELY));
    }

    @Test
    void calculateDailyCaloriesThrowExceptionForNegativeHeight() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(70, -1, 30, Gender.MALE, Activity.MODERATELY));
    }

    @Test
    void calculateDailyCaloriesThrowExceptionForNegativeAge() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(70, 170, -1, Gender.MALE, Activity.MODERATELY));
    }

    @Test
    void calculateDailyCaloriesCalculateForMaleSedentary() {
        double result = calculatorService.calculateDailyCalories(70, 170, 30, Gender.MALE, Activity.SEDENTARY);
        assertEquals(2006, result);
    }

    @Test
    void calculateDailyCaloriesCalculateForMaleLightlyActive() {
        double result = calculatorService.calculateDailyCalories(70, 170, 30, Gender.MALE, Activity.LIGHTLY);
        assertEquals(2299, result);
    }

    @Test
    void calculateDailyCaloriesCalculateForFemaleModeratelyActive() {
        double result = calculatorService.calculateDailyCalories(60, 165, 25, Gender.FEMALE, Activity.MODERATELY);
        assertEquals(2178, result);
    }

    @Test
    void calculateDailyCaloriesCalculateForFemaleVeryActive() {
        double result = calculatorService.calculateDailyCalories(55, 160, 35, Gender.FEMALE, Activity.VERY);
        assertEquals(2243, result);
    }

    @Test
    void calculateDailyCaloriesCalculateForMaleSuperActive() {
        double result = calculatorService.calculateDailyCalories(80, 180, 40, Gender.MALE, Activity.SUPER);
        assertEquals(3414, result);
    }
}