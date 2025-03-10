package hhh.meal_tracking.client.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserMealListRequest {

    private UUID id;
    private UUID userId;
    private List<UUID> mealsIds;
}