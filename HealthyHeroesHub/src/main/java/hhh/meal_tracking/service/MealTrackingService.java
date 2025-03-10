package hhh.meal_tracking.service;

import hhh.meal_tracking.client.MealTrackingClient;
import hhh.meal_tracking.client.dto.UserMealListRequest;
import hhh.meal_tracking.client.dto.AddMealToUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MealTrackingService {

    private final MealTrackingClient mealTrackingClient;

    @Autowired
    public MealTrackingService(MealTrackingClient mealTrackingClient) {
        this.mealTrackingClient = mealTrackingClient;
    }

    public void addMealToUser(UUID userId, UUID mealId) {
        AddMealToUserRequest request = new AddMealToUserRequest();
        request.setMealId(mealId);

        ResponseEntity<Void> response = mealTrackingClient.addMealToUser(userId, request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to add meal to user");
        }
    }

    public UserMealListRequest getUserMealList(UUID userId) {
        ResponseEntity<UserMealListRequest> response = mealTrackingClient.getUserMealList(userId);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch user meal list");
        }
    }
}