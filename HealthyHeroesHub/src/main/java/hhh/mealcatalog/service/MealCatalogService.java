package hhh.mealcatalog.service;
import hhh.mealcatalog.repository.MealCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealCatalogService {

    private final MealCatalogRepository mealCatalogRepository;

    @Autowired
    public MealCatalogService(MealCatalogRepository mealCatalogRepository) {
        this.mealCatalogRepository = mealCatalogRepository;
    }

}
