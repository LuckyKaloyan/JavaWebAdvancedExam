package hhh.meal_tracking.service;

import hhh.exception.MealTrackingException;
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
        if (userId == null) {
            throw new MealTrackingException("User ID cannot be null");
        }
        if (mealId == null) {
            throw new MealTrackingException("Meal ID cannot be null");
        }

        AddMealToUserRequest request = new AddMealToUserRequest();
        request.setMealId(mealId);

        ResponseEntity<Void> response = mealTrackingClient.addMealToUser(userId, request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new MealTrackingException("Failed to add meal to user. HTTP Status: " + response.getStatusCode());
        }
    }

    public UserMealListRequest getUserMealList(UUID userId) {
        if (userId == null) {
            throw new MealTrackingException("User ID cannot be null");
        }

        ResponseEntity<UserMealListRequest> response = mealTrackingClient.getUserMealList(userId);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new MealTrackingException("Failed to fetch user meal list. HTTP Status: " + response.getStatusCode());
        }
    }
}