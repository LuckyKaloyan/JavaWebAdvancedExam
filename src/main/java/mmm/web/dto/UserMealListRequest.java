package mmm.web.dto;

import java.util.List;
import java.util.UUID;

public class UserMealListRequest {

    private UUID userId;
    private List<UUID> mealsIds;

    // No-args constructor
    public UserMealListRequest() {
    }

    // All-args constructor
    public UserMealListRequest(UUID userId, List<UUID> mealsIds) {
        this.userId = userId;
        this.mealsIds = mealsIds;
    }

    // Getters and setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<UUID> getMealsIds() {
        return mealsIds;
    }

    public void setMealsIds(List<UUID> mealsIds) {
        this.mealsIds = mealsIds;
    }
}
