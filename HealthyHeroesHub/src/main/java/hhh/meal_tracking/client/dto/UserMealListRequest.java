package hhh.meal_tracking.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserMealListRequest {

    private UUID id;
    @NotNull
    private UUID userId;
    private List<UUID> mealsIds;
}