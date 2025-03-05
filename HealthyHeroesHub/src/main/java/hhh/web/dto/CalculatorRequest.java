package hhh.web.dto;

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

    @NotBlank(message = "Gender is required!")
    private String gender;

    @NotBlank(message = "Activity level is required!")
    private String activityLevel;

    @NotNull(message = "Eaten calories is required!")
    @Min(value = 0, message = "Eaten calories cannot be negative!")
    @Max(value = 99999, message = "You probably already died there...")
    private Double eatenCalories;
}