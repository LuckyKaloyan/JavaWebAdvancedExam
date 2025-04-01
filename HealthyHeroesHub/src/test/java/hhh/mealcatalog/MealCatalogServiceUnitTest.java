package hhh.mealcatalog;

import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.meal_tracking.client.MealTrackingClient;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.repository.MealCatalogRepository;
import hhh.mealcatalog.service.MealCatalogService;
import hhh.user.model.User;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.MealCatalogRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealCatalogServiceUnitTest {

    @Mock
    private MealCatalogRepository mealCatalogRepository;

    @Mock
    private MealTrackingClient mealTrackingClient;

    @InjectMocks
    private MealCatalogService mealCatalogService;

    @Test
    void createMealCatalog_shouldThrowExceptionWhenRequestIsNull() {
        User user = new User();
        assertThrows(BadInputException.class, () -> mealCatalogService.createMealCatalog(null, user));
    }

    @Test
    void createMealCatalog_shouldThrowExceptionWhenUserIsNull() {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("Test");
        assertThrows(BadInputException.class, () -> mealCatalogService.createMealCatalog(request, null));
    }

    @Test
    void createMealCatalog_shouldThrowExceptionWhenNameIsBlank() {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("");
        User user = new User();
        assertThrows(BadInputException.class, () -> mealCatalogService.createMealCatalog(request, user));
    }

    @Test
    void createMealCatalog_shouldSaveCatalog() {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("Test");
        request.setDescription("Desc");
        User user = new User();

        mealCatalogService.createMealCatalog(request, user);

        verify(mealCatalogRepository).save(any(MealCatalog.class));
    }

    @Test
    void getMealCatalogById_shouldThrowExceptionWhenIdIsNull() {
        assertThrows(BadInputException.class, () -> mealCatalogService.getMealCatalogById(null));
    }

    @Test
    void getMealCatalogById_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(mealCatalogRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> mealCatalogService.getMealCatalogById(id));
    }

    @Test
    void getMealCatalogById_shouldReturnCatalog() {
        UUID id = UUID.randomUUID();
        MealCatalog catalog = new MealCatalog();
        when(mealCatalogRepository.findById(id)).thenReturn(Optional.of(catalog));
        assertEquals(catalog, mealCatalogService.getMealCatalogById(id));
    }

    @Test
    void getAllMealCatalogs_shouldReturnAllCatalogs() {
        List<MealCatalog> catalogs = new ArrayList<>();
        when(mealCatalogRepository.findAll()).thenReturn(catalogs);
        assertEquals(catalogs, mealCatalogService.getAllMealCatalogs());
    }

    @Test
    void editMealCatalog_shouldThrowExceptionWhenIdIsNull() {
        EditCatalogRequest request = new EditCatalogRequest();
        request.setName("New");
        assertThrows(BadInputException.class, () -> mealCatalogService.editMealCatalog(null, request));
    }

    @Test
    void editMealCatalog_shouldThrowExceptionWhenRequestIsNull() {
        assertThrows(BadInputException.class, () -> mealCatalogService.editMealCatalog(UUID.randomUUID(), null));
    }

    @Test
    void editMealCatalog_shouldThrowExceptionWhenNameIsBlank() {
        EditCatalogRequest request = new EditCatalogRequest();
        request.setName("");
        assertThrows(BadInputException.class, () -> mealCatalogService.editMealCatalog(UUID.randomUUID(), request));
    }

    @Test
    void editMealCatalog_shouldUpdateCatalog() {
        UUID id = UUID.randomUUID();
        EditCatalogRequest request = new EditCatalogRequest();
        request.setName("New");
        request.setDescription("New Desc");

        MealCatalog catalog = new MealCatalog();
        when(mealCatalogRepository.findById(id)).thenReturn(Optional.of(catalog));

        mealCatalogService.editMealCatalog(id, request);

        assertEquals("New", catalog.getName());
        assertEquals("New Desc", catalog.getDescription());
        verify(mealCatalogRepository).save(catalog);
    }

    @Test
    void deleteCatalog_shouldThrowExceptionWhenIdIsNull() {
        assertThrows(BadInputException.class, () -> mealCatalogService.deleteCatalog(null));
    }

}