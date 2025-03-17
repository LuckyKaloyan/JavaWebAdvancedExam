package hhh.calculator;

import hhh.exception.BadInputException;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculateDailyCalories(double weight, double height, int age, String gender, String activityLevel) {
        if (weight <= 0 || height <= 0 || age <= 0) {
            throw new BadInputException("Invalid input parameters. Weight, height, and age must be positive, and eatenCalories must be non-negative.");
        }
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
            throw new BadInputException("Invalid gender. Must be 'male' or 'female'.");
        }
        double bmr;
        if (gender.equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        double activityMultiplier = switch (activityLevel.trim().toLowerCase()) {
            case "sedentary" -> 1.2;
            case "lightly" -> 1.375;
            case "moderately" -> 1.55;
            case "very" -> 1.725;
            case "super" -> 1.9;
            default -> throw new BadInputException("Invalid activity level: " + activityLevel);
        };

        return (Math.round(bmr * activityMultiplier));
    }
}