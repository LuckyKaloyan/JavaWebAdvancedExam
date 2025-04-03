package hhh.meal_tracking;

import hhh.exception.MealTrackingException;
import hhh.meal_tracking.client.MealTrackingClient;
import hhh.meal_tracking.client.dto.UserMealListRequest;
import hhh.meal_tracking.client.dto.AddMealToUserRequest;
import hhh.meal_tracking.service.MealTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealTrackingServiceUnitTest {

    @Mock
    private MealTrackingClient mealTrackingClient;

    @InjectMocks
    private MealTrackingService mealTrackingService;

    private UUID validUserId;
    private UUID validMealId;

    @BeforeEach
    void setUp() {
        validUserId = UUID.randomUUID();
        validMealId = UUID.randomUUID();
    }

    @Test
    void addMealToUserSuccessWhenValidInputs() {
        ResponseEntity<Void> successResponse = ResponseEntity.ok().build();
        when(mealTrackingClient.addMealToUser(any(UUID.class), any(AddMealToUserRequest.class))).thenReturn(successResponse);
        assertDoesNotThrow(() -> mealTrackingService.addMealToUser(validUserId, validMealId));
    }

    @Test
    void addMealToUserThrowExceptionWhenUserIdIsNull() {
        MealTrackingException exception = assertThrows(MealTrackingException.class, () -> mealTrackingService.addMealToUser(null, validMealId));
        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void addMealToUserThrowExceptionWhenMealIdIsNull() {
        MealTrackingException exception = assertThrows(MealTrackingException.class, () -> mealTrackingService.addMealToUser(validUserId, null));
        assertEquals("Meal ID cannot be null", exception.getMessage());
    }

    @Test
    void getUserMealListReturnUserMealListWhenValidUserId() {
        UserMealListRequest expectedResponse = new UserMealListRequest();
        ResponseEntity<UserMealListRequest> successResponse = ResponseEntity.ok(expectedResponse);
        when(mealTrackingClient.getUserMealList(validUserId)).thenReturn(successResponse);
        UserMealListRequest result = mealTrackingService.getUserMealList(validUserId);
        assertSame(expectedResponse, result);
    }

    @Test
    void getUserMealListThrowExceptionWhenUserIdIsNull() {
        MealTrackingException exception = assertThrows(MealTrackingException.class, () -> mealTrackingService.getUserMealList(null));
        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void getUserMealListThrowExceptionWhenClientReturnsError() {
        ResponseEntity<UserMealListRequest> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(mealTrackingClient.getUserMealList(validUserId)).thenReturn(errorResponse);
        MealTrackingException exception = assertThrows(MealTrackingException.class, () -> mealTrackingService.getUserMealList(validUserId));
        assertTrue(exception.getMessage().contains("Failed to fetch user meal list"));
        assertTrue(exception.getMessage().contains("404"));
    }

    @Test
    void getUserMealListThrowExceptionWhenResponseBodyIsNull() {
        ResponseEntity<UserMealListRequest> successResponseWithNullBody = ResponseEntity.ok().build();
        when(mealTrackingClient.getUserMealList(validUserId)).thenReturn(successResponseWithNullBody);
        MealTrackingException exception = assertThrows(MealTrackingException.class, () -> mealTrackingService.getUserMealList(validUserId));
        assertTrue(exception.getMessage().contains("Failed to fetch user meal list"));
    }
}