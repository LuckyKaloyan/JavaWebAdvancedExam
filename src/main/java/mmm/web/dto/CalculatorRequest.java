package mmm.web.dto;

import mmm.calculator.Activity;
import mmm.calculator.Gender;
import jakarta.validation.constraints.*;

public class CalculatorRequest {

    @Min(value = 10, message = "Weight must be at least 10 kg!")
    @Max(value = 300, message = "Weight cannot exceed 300 kg!")
    private double weight;

    @Min(value = 30, message = "Height must be at least 30 cm!")
    @Max(value = 300, message = "Height cannot exceed 300 cm!")
    private double height;

    @Min(value = 1, message = "Age must be at least 1 year!")
    private int age;

    @NotNull(message = "Gender is required!")
    private Gender gender;

    @NotNull(message = "Activity level is required!")
    private Activity activityLevel;

    // No-args constructor
    public CalculatorRequest() {
    }

    // All-args constructor
    public CalculatorRequest(
            double weight,
            double height,
            int age,
            Gender gender,
            Activity activityLevel
    ) {
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }

    // Getters and setters

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Activity getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Activity activityLevel) {
        this.activityLevel = activityLevel;
    }
}
