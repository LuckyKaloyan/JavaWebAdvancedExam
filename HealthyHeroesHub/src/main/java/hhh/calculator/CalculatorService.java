package hhh.calculator;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public String calculateDailyCalories(double weight, double height, int age, String gender, String activityLevel, double eatenCalories) {
        if (weight <= 0 || height <= 0 || age <= 0 || eatenCalories < 0) {
            throw new IllegalArgumentException("Invalid input parameters. Weight, height, and age must be positive, and eatenCalories must be non-negative.");
        }
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
            throw new IllegalArgumentException("Invalid gender. Must be 'male' or 'female'.");
        }
        double bmr;
        if (gender.equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        double activityMultiplier = switch (activityLevel.trim().toLowerCase()) {
            case "sedentary" -> 1.2;
            case "lightly_active", "lightly active" -> 1.375;
            case "moderately_active", "moderately active" -> 1.55;
            case "very_active", "very active" -> 1.725;
            case "super_active", "super active" -> 1.9;
            default -> throw new IllegalArgumentException("Invalid activity level: " + activityLevel);
        };

        double totalCaloriesNeeded = bmr * activityMultiplier;
        double difference = totalCaloriesNeeded - eatenCalories;
        difference = Math.round(difference * 100) / 100.0;

        if (difference == 0) {
            return "You are perfectly on track with your calorie intake!";
        } else if (difference > 0) {
            return String.format("You are energy deficient. You need to take %.2f calories more!", difference);
        } else {
            return String.format("You are eating too much. You are %.2f calories above what you need!", -difference);
        }
    }
}