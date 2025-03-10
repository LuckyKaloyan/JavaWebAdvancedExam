package hhh.meal_tracking.client.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddMealToUserRequest {

    private UUID mealId;
}