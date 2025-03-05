package hhh.meal.service;
import hhh.meal.model.FavouriteMeal;
import hhh.meal.model.Meal;
import hhh.meal.repository.FavouriteMealRepository;
import hhh.meal.repository.MealRepository;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.MealRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final FavouriteMealRepository favouriteMealRepository;
    private final UserService userService;

    @Autowired
    public MealService(MealRepository mealRepository, FavouriteMealRepository favouriteMealRepository, UserService userService) {
        this.mealRepository = mealRepository;
        this.favouriteMealRepository = favouriteMealRepository;
        this.userService = userService;
    }

    public void addMeal(MealRequest mealRequest, MealCatalog mealCatalog) {
        BigDecimal carbsCalories = mealRequest.getCarbs().multiply(BigDecimal.valueOf(4));
        BigDecimal proteinsCalories = mealRequest.getProteins().multiply(BigDecimal.valueOf(4));
        BigDecimal fatsCalories = mealRequest.getFats().multiply(BigDecimal.valueOf(8));
        BigDecimal totalCalories = carbsCalories.add(proteinsCalories).add(fatsCalories);

        Meal meal = Meal.builder()
                .name(mealRequest.getName())
                .description(mealRequest.getDescription())
                .fats(mealRequest.getFats())
                .carbs(mealRequest.getCarbs())
                .proteins(mealRequest.getProteins())
                .totalCalories(totalCalories)
                .picture(mealRequest.getPicture())
                .addedOn(LocalDate.now())
                .mealCatalog(mealCatalog)
                .owner(mealCatalog.getOwner())
                .build();

        if(meal.getPicture().isBlank() && meal.getPicture().isEmpty()){
            meal.setPicture("https://www.shutterstock.com/image-vector/eating-icon-vector-spoon-plate-600nw-1509460529.jpg");
        }

        mealRepository.save(meal);
    }

    public Meal getMealById(UUID id) {
        return mealRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid MEAL ID"));
    }

    public void deleteMealById(UUID id) {
        mealRepository.deleteById(id);
    }


    public FavouriteMeal createFavouriteMeal(User user, Meal meal) {
        FavouriteMeal favouriteMeal = FavouriteMeal.builder()
                .user(user)
                .meal(meal)
                .favouritedOn(LocalDate.now())
                .build();
        return favouriteMealRepository.save(favouriteMeal);
    }

    public void addFavouriteMeal(UUID mealId, UUID userId) {
        User user = userService.getById(userId);
        Meal meal = getMealById(mealId);
        if (favouriteMealRepository.findByUserAndMeal(user, meal).isPresent()) {
            throw new RuntimeException("You can Favourite a meal only once... Come on!");
        }
        user.getFavouriteMeals().add(createFavouriteMeal(user, meal));
    }

    public void deleteFavouriteMeal(UUID favouriteMealId) {
        favouriteMealRepository.deleteById(favouriteMealId);
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public List<Meal> getAllAddedLastMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        return mealRepository.findByAddedOnAfter(oneMonthAgo);
    }

    public List<Meal> getAllAddedLastYear() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return mealRepository.findByAddedOnAfter(oneYearAgo);
    }

    public List<Meal> getAllAddedLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        return mealRepository.findByAddedOnAfter(oneWeekAgo);
    }

    public List<Meal> getAllAddedLast24hours() {
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
        return mealRepository.findByAddedOnAfter(oneDayAgo);
    }

    public List<Meal> getTop20Meals() {
        return mealRepository.findTop20ByOrderByUpVotesDesc();
    }
}
