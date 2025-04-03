package hhh.web.dto;
import hhh.calculator.Activity;
import hhh.calculator.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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

}