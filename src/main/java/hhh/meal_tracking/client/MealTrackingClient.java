package hhh.meal_tracking.client;

import hhh.meal_tracking.client.dto.AddMealToUserRequest;
import hhh.meal_tracking.client.dto.UserMealListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@FeignClient(name = "meal-tracker-service", url = "https://mymealmetermicroservice.azurewebsites.net/api/v1/meal_tracker")
public interface MealTrackingClient {

    @PostMapping("/{userId}")
    ResponseEntity<Void> addMealToUser(@PathVariable UUID userId, @RequestBody AddMealToUserRequest request);
    @GetMapping("/{userId}")
    ResponseEntity<UserMealListRequest> getUserMealList(@PathVariable UUID userId);
    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteMealFromUser(@PathVariable UUID userId, @RequestParam int mealIndex);
    @DeleteMapping("/removeMealFromAllUsers/{mealId}")
    ResponseEntity<Void> removeMealFromAllUsers(@PathVariable UUID mealId);

}