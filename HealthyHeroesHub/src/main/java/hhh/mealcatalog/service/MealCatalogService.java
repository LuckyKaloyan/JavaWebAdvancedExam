package hhh.mealcatalog.service;
import hhh.meal.model.Meal;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.repository.MealCatalogRepository;
import hhh.report.model.Report;
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

        MealCatalog mealCatalog = MealCatalog.builder()
               .name(mealCatalogRequest.getName())
               .description(mealCatalogRequest.getDescription())
               .owner(user)
               .addedOn(LocalDate.now())
               .lastUpdate(LocalDate.now())
               .meals(listMeals)
               .build();


        this.mealCatalogRepository.save(mealCatalog);
    }

    public MealCatalog getMealCatalogById(UUID id) {
        return this.mealCatalogRepository.findById(id).orElseThrow(() -> new RuntimeException("INVALID ID BABE"));
    }
    public List<MealCatalog> getAllMealCatalogs() {
        return this.mealCatalogRepository.findAll();
    }
    public void editMealCatalog(UUID mealCatalogId, EditCatalogRequest editCatalogRequest) {
          MealCatalog mealCatalog = getMealCatalogById(mealCatalogId);
          mealCatalog.setName(editCatalogRequest.getName());
          mealCatalog.setDescription(editCatalogRequest.getDescription());
          mealCatalogRepository.save(mealCatalog);
    }
    public void deleteCatalog(UUID mealCatalogId) {
        mealCatalogRepository.deleteById(mealCatalogId);
    }
}
