package hhh.mealcatalog.service;

import hhh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MealCatalogInit implements CommandLineRunner {

    private final MealCatalogService mealCatalogService;
    private final UserService userService;


    @Autowired
    public MealCatalogInit(MealCatalogService mealCatalogService, UserService userService) {
        this.mealCatalogService = mealCatalogService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userService.getAll().isEmpty()){
            userService.createDefaultAdmin();
        }

        if(mealCatalogService.getAllMealCatalogs().isEmpty()){
            mealCatalogService.createDefaultCatalog();
        }
    }
}
