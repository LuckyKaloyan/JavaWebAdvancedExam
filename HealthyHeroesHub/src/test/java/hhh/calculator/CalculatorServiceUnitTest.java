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
    void calculateDailyCalories_shouldThrowExceptionForNegativeWeight() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(-1, 170, 30, "male", "moderately"));
    }

    @Test
    void calculateDailyCalories_shouldThrowExceptionForNegativeHeight() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(70, -1, 30, "male", "moderately"));
    }

    @Test
    void calculateDailyCalories_shouldThrowExceptionForNegativeAge() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(70, 170, -1, "male", "moderately"));
    }

    @Test
    void calculateDailyCalories_shouldThrowExceptionForInvalidGender() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(70, 170, 30, "invalid", "moderately"));
    }

    @Test
    void calculateDailyCalories_shouldThrowExceptionForInvalidActivityLevel() {
        assertThrows(BadInputException.class, () ->
                calculatorService.calculateDailyCalories(70, 170, 30, "male", "invalid"));
    }

    @Test
    void calculateDailyCalories_shouldCalculateForMaleSedentary() {
        double result = calculatorService.calculateDailyCalories(70, 170, 30, "male", "sedentary");
        assertEquals(2006, result);
    }

    @Test
    void calculateDailyCalories_shouldCalculateForMaleLightlyActive() {
        double result = calculatorService.calculateDailyCalories(70, 170, 30, "male", "lightly");
        assertEquals(2299, result);
    }

    @Test
    void calculateDailyCalories_shouldCalculateForFemaleModeratelyActive() {
        double result = calculatorService.calculateDailyCalories(60, 165, 25, "female", "moderately");
        assertEquals(2178, result);
    }

    @Test
    void calculateDailyCalories_shouldCalculateForFemaleVeryActive() {
        double result = calculatorService.calculateDailyCalories(55, 160, 35, "female", "very");
        assertEquals(2243, result);
    }

    @Test
    void calculateDailyCalories_shouldCalculateForMaleSuperActive() {
        double result = calculatorService.calculateDailyCalories(80, 180, 40, "male", "super");
        assertEquals(3414, result);
    }

    @Test
    void calculateDailyCalories_shouldBeCaseInsensitiveForGender() {
        double result1 = calculatorService.calculateDailyCalories(70, 170, 30, "MALE", "moderately");
        double result2 = calculatorService.calculateDailyCalories(70, 170, 30, "male", "moderately");
        assertEquals(result1, result2);
    }

    @Test
    void calculateDailyCalories_shouldBeCaseInsensitiveForActivityLevel() {
        double result1 = calculatorService.calculateDailyCalories(70, 170, 30, "male", "MODERATELY");
        double result2 = calculatorService.calculateDailyCalories(70, 170, 30, "male", "moderately");
        assertEquals(result1, result2);
    }
}