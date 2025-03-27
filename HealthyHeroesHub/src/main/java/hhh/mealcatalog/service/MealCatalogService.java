package hhh.mealcatalog.service;
import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.repository.MealCatalogRepository;
import hhh.user.model.User;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.MealCatalogRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MealCatalogService {

    private final MealCatalogRepository mealCatalogRepository;

    @Autowired
    public MealCatalogService(MealCatalogRepository mealCatalogRepository) {
        this.mealCatalogRepository = mealCatalogRepository;
    }

    public void createMealCatalog(@Valid MealCatalogRequest mealCatalogRequest, User user) {
        List<Meal> listMeals = new ArrayList<>();
        if (mealCatalogRequest == null) {
            throw new BadInputException("MealCatalogRequest cannot be null");
        }
        if (user == null) {
            throw new BadInputException("User cannot be null");
        }
        if (mealCatalogRequest.getName() == null || mealCatalogRequest.getName().isBlank()) {
            throw new BadInputException("Meal catalog name cannot be empty");
        }

        MealCatalog mealCatalog = MealCatalog.builder()
               .name(mealCatalogRequest.getName())
               .description(mealCatalogRequest.getDescription())
               .owner(user)
               .addedOn(LocalDate.now())
               .type(mealCatalogRequest.getType())
               .meals(listMeals)
               .build();

        this.mealCatalogRepository.save(mealCatalog);
    }

    public MealCatalog getMealCatalogById(UUID id) {

        if (id == null) {
            throw new BadInputException("Meal catalog ID cannot be null");
        }
        return this.mealCatalogRepository.findById(id)
                .orElseThrow(() -> new BadInputException("Meal catalog not found with ID: " + id));
    }
    public List<MealCatalog> getAllMealCatalogs() {
        return this.mealCatalogRepository.findAll();
    }
    public void editMealCatalog(UUID mealCatalogId, EditCatalogRequest editCatalogRequest) {
        if (mealCatalogId == null) {
            throw new BadInputException("Meal catalog ID cannot be null");
        }
        if (editCatalogRequest == null) {
            throw new BadInputException("EditCatalogRequest cannot be null");
        }
        if (editCatalogRequest.getName() == null || editCatalogRequest.getName().isBlank()) {
            throw new BadInputException("Meal catalog name cannot be empty");
        }
          MealCatalog mealCatalog = getMealCatalogById(mealCatalogId);
          mealCatalog.setName(editCatalogRequest.getName());
          mealCatalog.setDescription(editCatalogRequest.getDescription());
          mealCatalogRepository.save(mealCatalog);
    }
    public void deleteCatalog(UUID mealCatalogId) {
        if (mealCatalogId == null) {
            throw new BadInputException("Meal catalog ID cannot be null");
        }
        mealCatalogRepository.deleteById(mealCatalogId);
    }
}
