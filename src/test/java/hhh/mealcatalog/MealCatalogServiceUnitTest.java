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
    void createMealCatalogThrowExceptionWhenRequestIsNull() {
        User user = new User();
        assertThrows(BadInputException.class, () -> mealCatalogService.createMealCatalog(null, user));
    }

    @Test
    void createMealCatalogThrowExceptionWhenUserIsNull() {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("Test");
        assertThrows(BadInputException.class, () -> mealCatalogService.createMealCatalog(request, null));
    }

    @Test
    void createMealCatalogThrowExceptionWhenNameIsBlank() {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("");
        User user = new User();
        assertThrows(BadInputException.class, () -> mealCatalogService.createMealCatalog(request, user));
    }

    @Test
    void createMealCatalogSaveCatalog() {
        MealCatalogRequest request = new MealCatalogRequest();
        request.setName("Test");
        request.setDescription("Desc");
        User user = new User();

        mealCatalogService.createMealCatalog(request, user);

        verify(mealCatalogRepository).save(any(MealCatalog.class));
    }

    @Test
    void getMealCatalogByIdThrowExceptionWhenIdIsNull() {
        assertThrows(BadInputException.class, () -> mealCatalogService.getMealCatalogById(null));
    }

    @Test
    void getMealCatalogByIdThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(mealCatalogRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BadInputException.class, () -> mealCatalogService.getMealCatalogById(id));
    }

    @Test
    void getMealCatalogByIdReturnCatalog() {
        UUID id = UUID.randomUUID();
        MealCatalog catalog = new MealCatalog();
        when(mealCatalogRepository.findById(id)).thenReturn(Optional.of(catalog));
        assertEquals(catalog, mealCatalogService.getMealCatalogById(id));
    }

    @Test
    void getAllMealCatalogsReturnAllCatalogs() {
        List<MealCatalog> catalogs = new ArrayList<>();
        when(mealCatalogRepository.findAll()).thenReturn(catalogs);
        assertEquals(catalogs, mealCatalogService.getAllMealCatalogs());
    }

    @Test
    void editMealCatalogThrowExceptionWhenIdIsNull() {
        EditCatalogRequest request = new EditCatalogRequest();
        request.setName("New");
        assertThrows(BadInputException.class, () -> mealCatalogService.editMealCatalog(null, request));
    }

    @Test
    void editMealCatalogThrowExceptionWhenRequestIsNull() {
        assertThrows(BadInputException.class, () -> mealCatalogService.editMealCatalog(UUID.randomUUID(), null));
    }

    @Test
    void editMealCatalogThrowExceptionWhenNameIsBlank() {
        EditCatalogRequest request = new EditCatalogRequest();
        request.setName("");
        assertThrows(BadInputException.class, () -> mealCatalogService.editMealCatalog(UUID.randomUUID(), request));
    }

    @Test
    void editMealCatalogUpdateCatalog() {
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
    void deleteCatalogThrowExceptionWhenIdIsNull() {
        assertThrows(BadInputException.class, () -> mealCatalogService.deleteCatalog(null));
    }

}