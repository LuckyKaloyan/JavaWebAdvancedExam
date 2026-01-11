package mmm.calculator;

import mmm.exception.BadInputException;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculateDailyCalories(double weight, double height, int age, Gender gender, Activity activityLevel) {
        if (weight <= 0 || height <= 0 || age <= 0) {
            throw new BadInputException("Invalid input parameters. Weight, height, and age must be positive, and eatenCalories must be non-negative.");
        }

        double bmr;
        if (gender == Gender.MALE) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        double activityMultiplier = switch (activityLevel) {
            case SEDENTARY -> 1.2;
            case LIGHTLY -> 1.375;
            case MODERATELY -> 1.55;
            case VERY -> 1.725;
            case SUPER -> 1.9;
        };

        return Math.round(bmr * activityMultiplier);
    }
}