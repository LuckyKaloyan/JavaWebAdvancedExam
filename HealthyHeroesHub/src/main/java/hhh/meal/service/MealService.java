package hhh.meal.service;
import hhh.exception.AlreadyFavouriteException;
import hhh.exception.BadInputException;
import hhh.meal.model.FavouriteMeal;
import hhh.meal.model.Meal;
import hhh.meal.model.MealOfTheHour;
import hhh.meal.repository.FavouriteMealRepository;
import hhh.meal.repository.MealOfTheHourRepository;
import hhh.meal.repository.MealRepository;
import hhh.meal_tracking.client.MealTrackingClient;
import hhh.mealcatalog.model.MealCatalog;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.MealRequest;
import hhh.web.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final FavouriteMealRepository favouriteMealRepository;
    private final UserService userService;
    private final MealTrackingClient mealTrackingClient;



    @Autowired
    public MealService(MealRepository mealRepository, FavouriteMealRepository favouriteMealRepository, UserService userService, MealTrackingClient mealTrackingClient) {
        this.mealRepository = mealRepository;
        this.favouriteMealRepository = favouriteMealRepository;
        this.userService = userService;
        this.mealTrackingClient = mealTrackingClient;
    }

    public void addMeal(MealRequest mealRequest, MealCatalog mealCatalog) {
        if (mealRequest == null) {
            throw new BadInputException("MealRequest cannot be null");
        }
        if (mealCatalog == null) {
            throw new BadInputException("MealCatalog cannot be null");
        }
        if (mealRequest.getName() == null || mealRequest.getName().isBlank()) {
            throw new BadInputException("Meal name cannot be empty");
        }
        if (mealRequest.getCarbs() == null || mealRequest.getCarbs().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadInputException("Carbs cannot be null or negative");
        }
        if (mealRequest.getProteins() == null || mealRequest.getProteins().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadInputException("Proteins cannot be null or negative");
        }
        if (mealRequest.getFats() == null || mealRequest.getFats().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadInputException("Fats cannot be null or negative");
        }
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
        return mealRepository.findById(id).orElseThrow(() -> new BadInputException("Invalid MEAL ID"));
    }

    public void deleteMealById(UUID id) {
        mealRepository.deleteById(id);
        ResponseEntity<Void> response = mealTrackingClient.removeMealFromAllUsers(id);
    }


    public FavouriteMeal createFavouriteMeal(User user, Meal meal) {
        if (user == null) {
            throw new BadInputException("User cannot be null");
        }
        if (meal == null) {
            throw new BadInputException("Meal cannot be null");
        }
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
            throw new AlreadyFavouriteException("Meal is already favourite");
        }
        user.getFavouriteMeals().add(createFavouriteMeal(user, meal));
    }

    public void deleteFavouriteMeal(UUID favouriteMealId) {
        if(favouriteMealRepository.findById(favouriteMealId).isEmpty()){
            throw new BadInputException("Invalid favourite meal id");
        }
        favouriteMealRepository.deleteById(favouriteMealId);
    }
    public void unFavouriteMeal(UUID mealId, UUID userId) {
        User user = userService.getById(userId);
        Meal meal = getMealById(mealId);
        if(user == null){
            throw new BadInputException("User cannot be null");
        }
        if(meal == null){
            throw new BadInputException("Meal cannot be null");
        }
        if(favouriteMealRepository.findByUserAndMeal(user, meal).isPresent()){
            favouriteMealRepository.delete(favouriteMealRepository.findByUserAndMeal(user, meal).get());
        }
    }
    public boolean isFavourite(UUID mealId, UUID userId) {
        User user = userService.getById(userId);
        Meal meal = getMealById(mealId);
        if(user == null){
            throw new BadInputException("User cannot be null");
        }
        if(meal == null){
            throw new BadInputException("Meal cannot be null");
        }
        if(favouriteMealRepository.findByUserAndMeal(user, meal).isPresent()){
            return true;
        }
        else {
            return false;
        }

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

    public List<Meal> mealsList(List<UUID> ids) {
        List<Meal> meals = mealRepository.findAllById(ids);
        Map<UUID, Meal> mealMap = meals.stream().collect(Collectors.toMap(Meal::getId, meal -> meal));
        return ids.stream()
                .map(mealMap::get)
                .collect(Collectors.toList());
    }

    public double totalMealsCalories(List<Meal> someList){
        double total;
        total = 0;
        for(Meal meal : someList){
            if(meal!=null){
                total = total + Double.parseDouble(meal.getTotalCalories().toString());
            }
        }
        return total;
    }
}