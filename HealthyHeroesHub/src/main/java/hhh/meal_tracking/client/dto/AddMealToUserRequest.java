package hhh.meal_tracking.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddMealToUserRequest {

    @NotNull
    private UUID mealId;
}