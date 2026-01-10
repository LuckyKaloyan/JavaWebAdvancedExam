package hhh.mealcatalog.service;

import hhh.eatenmealslist.service.EatenMealsListService;
import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.mealcatalog.model.MealCatalog;
import hhh.mealcatalog.model.MealCatalogType;
import hhh.mealcatalog.repository.MealCatalogRepository;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.web.dto.EditCatalogRequest;
import hhh.web.dto.MealCatalogRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MealCatalogService {

    private final MealCatalogRepository mealCatalogRepository;
    private final UserService userService;
    private final EatenMealsListService eatenMealsListService;

    @Autowired
    public MealCatalogService(MealCatalogRepository mealCatalogRepository,
                              UserService userService,
                              EatenMealsListService eatenMealsListService) {
        this.mealCatalogRepository = mealCatalogRepository;
        this.userService = userService;
        this.eatenMealsListService = eatenMealsListService;
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

        MealCatalog mealCatalog = new MealCatalog();
        mealCatalog.setName(mealCatalogRequest.getName());
        mealCatalog.setDescription(mealCatalogRequest.getDescription());
        mealCatalog.setOwner(user);
        mealCatalog.setAddedOn(LocalDate.now());
        mealCatalog.setType(mealCatalogRequest.getType());
        mealCatalog.setMeals(listMeals);

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

        for (Meal meal : mealCatalogRepository.getMealCatalogById(mealCatalogId).get().getMeals()) {
            eatenMealsListService.deleteAllMealsWithId(meal.getId());
        }

        mealCatalogRepository.deleteById(mealCatalogId);
    }

    @Transactional
    public void createDefaultCatalog() {
        User owner = userService.getByUsername("luckykaloyan");

        MealCatalog firstCatalog = new MealCatalog();
        firstCatalog.setOwner(owner);
        firstCatalog.setDescription("This exclusive omnivore meal catalog showcases a curated selection of the finest mixed dishes, thoughtfully compiled and presented by the admin!");
        firstCatalog.setName("LuckyKaloyan's omnivore meal catalog.");
        firstCatalog.setAddedOn(LocalDate.now());
        firstCatalog.setMeals(new ArrayList<>());
        firstCatalog.setType(MealCatalogType.OMNIVORE);

        Meal omnivoreBreakfast = new Meal();
        omnivoreBreakfast.setOwner(owner);
        omnivoreBreakfast.setProteins(BigDecimal.valueOf(5));
        omnivoreBreakfast.setCarbs(BigDecimal.valueOf(15));
        omnivoreBreakfast.setFats(BigDecimal.valueOf(4));
        omnivoreBreakfast.setTotalCalories(BigDecimal.valueOf(112));
        omnivoreBreakfast.setDescription("A healthy and filling dish combining oats and yogurt, rich in fiber and protein.");
        omnivoreBreakfast.setName("Oats with yogurt(per 100 gr.).");
        omnivoreBreakfast.setAddedOn(LocalDate.now());
        omnivoreBreakfast.setPicture("https://www.luvele.co.uk/cdn/shop/articles/oat_milk_yogurt_06_1024x.png?v=1718168524");
        omnivoreBreakfast.setMealCatalog(firstCatalog);

        Meal omnivoreLunch = new Meal();
        omnivoreLunch.setOwner(owner);
        omnivoreLunch.setProteins(BigDecimal.valueOf(40));
        omnivoreLunch.setCarbs(BigDecimal.valueOf(40));
        omnivoreLunch.setFats(BigDecimal.valueOf(25));
        omnivoreLunch.setTotalCalories(BigDecimal.valueOf(420));
        omnivoreLunch.setDescription("A nutritious and balanced meal with spinach, eggs, pork, butter, and mixed vegetables, providing protein, healthy fats, and essential vitamins.");
        omnivoreLunch.setName("Spinach, Eggs, and Pork Lunch(per 450 gr.).");
        omnivoreLunch.setAddedOn(LocalDate.now());
        omnivoreLunch.setPicture("https://thumb.photo-ac.com/9c/9cd109f8ae164a3b13f5e0a40824611f_t.jpeg");
        omnivoreLunch.setMealCatalog(firstCatalog);

        Meal omnivoreDinner = new Meal();
        omnivoreDinner.setOwner(owner);
        omnivoreDinner.setProteins(BigDecimal.valueOf(35));
        omnivoreDinner.setCarbs(BigDecimal.valueOf(10));
        omnivoreDinner.setFats(BigDecimal.valueOf(22));
        omnivoreDinner.setTotalCalories(BigDecimal.valueOf(356));
        omnivoreDinner.setDescription("A healthy and satisfying meal featuring grilled salmon and a variety of roasted or steamed vegetables, rich in omega-3 fatty acids, protein, and essential vitamins.");
        omnivoreDinner.setName("Salmon with Vegetables Dinner(per 250 gr.).");
        omnivoreDinner.setAddedOn(LocalDate.now());
        omnivoreDinner.setPicture("https://www.theroastedroot.net/wp-content/uploads/2020/02/one_pan_salmon_and_vegetables_1.jpg");
        omnivoreDinner.setMealCatalog(firstCatalog);

        firstCatalog.getMeals().add(omnivoreBreakfast);
        firstCatalog.getMeals().add(omnivoreLunch);
        firstCatalog.getMeals().add(omnivoreDinner);
        mealCatalogRepository.save(firstCatalog);

        MealCatalog secondCatalog = new MealCatalog();
        secondCatalog.setOwner(owner);
        secondCatalog.setDescription("This exclusive meal catalog showcases a curated selection of the finest vegan dishes, thoughtfully compiled and presented by the admin!");
        secondCatalog.setName("LuckyKaloyan's vegan meal catalog.");
        secondCatalog.setAddedOn(LocalDate.now());
        secondCatalog.setMeals(new ArrayList<>());
        secondCatalog.setType(MealCatalogType.VEGAN);

        Meal veganBreakfast = new Meal();
        veganBreakfast.setOwner(owner);
        veganBreakfast.setProteins(BigDecimal.valueOf(6));
        veganBreakfast.setCarbs(BigDecimal.valueOf(25));
        veganBreakfast.setFats(BigDecimal.valueOf(15));
        veganBreakfast.setTotalCalories(BigDecimal.valueOf(244));
        veganBreakfast.setDescription("A creamy and nutrient-packed breakfast made with chia seeds, almond milk, and topped with fresh berries for added flavor and antioxidants.");
        veganBreakfast.setName("Chia Pudding with Berries(per 100 gr.).");
        veganBreakfast.setAddedOn(LocalDate.now());
        veganBreakfast.setPicture("https://www.thehealthymaven.com/wp-content/uploads/2016/03/Verry-Berry-Chia-Pudding-6.png");
        veganBreakfast.setMealCatalog(secondCatalog);

        Meal veganLunch = new Meal();
        veganLunch.setOwner(owner);
        veganLunch.setProteins(BigDecimal.valueOf(12));
        veganLunch.setCarbs(BigDecimal.valueOf(40));
        veganLunch.setFats(BigDecimal.valueOf(18));
        veganLunch.setTotalCalories(BigDecimal.valueOf(352));
        veganLunch.setDescription("A light yet filling salad with quinoa, chickpeas, avocado, cucumber, and a lemon-tahini dressing, packed with protein, healthy fats, and fiber.");
        veganLunch.setName("Quinoa Salad with Chickpeas and Avocado");
        veganLunch.setAddedOn(LocalDate.now());
        veganLunch.setPicture("https://www.eatingwell.com/thmb/lYBxQk2CC-BPJpnHO3foW4KjWes=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/6859253-83bb7ca335f046d694de9aa8042eaf0a.jpg");
        veganLunch.setMealCatalog(secondCatalog);

        Meal veganDinner = new Meal();
        veganDinner.setOwner(owner);
        veganDinner.setProteins(BigDecimal.valueOf(18));
        veganDinner.setCarbs(BigDecimal.valueOf(40));
        veganDinner.setFats(BigDecimal.valueOf(10));
        veganDinner.setTotalCalories(BigDecimal.valueOf(312));
        veganDinner.setDescription("A savory stir-fry made with lentils, mixed vegetables (such as bell peppers, carrots, and spinach), and a soy-ginger sauce for a delicious, protein-rich meal.");
        veganDinner.setName("Lentil and Vegetable Stir-Fry");
        veganDinner.setAddedOn(LocalDate.now());
        veganDinner.setPicture("https://www.theroastedroot.net/wp-content/uploads/2020/02/one_pan_salmon_and_vegetables_1.jpg");
        veganDinner.setMealCatalog(secondCatalog);

        secondCatalog.getMeals().add(veganBreakfast);
        secondCatalog.getMeals().add(veganLunch);
        secondCatalog.getMeals().add(veganDinner);
        mealCatalogRepository.save(secondCatalog);

        MealCatalog thirdCatalog = new MealCatalog();
        thirdCatalog.setOwner(owner);
        thirdCatalog.setDescription("This exclusive meal catalog showcases a curated selection of the finest carnivore dishes, thoughtfully compiled and presented by the admin!");
        thirdCatalog.setName("LuckyKaloyan's carnivore meal catalog.");
        thirdCatalog.setAddedOn(LocalDate.now());
        thirdCatalog.setMeals(new ArrayList<>());
        thirdCatalog.setType(MealCatalogType.CARNIVORE);

        Meal carnivoreBreakfast = new Meal();
        carnivoreBreakfast.setOwner(owner);
        carnivoreBreakfast.setProteins(BigDecimal.valueOf(20));
        carnivoreBreakfast.setCarbs(BigDecimal.valueOf(0));
        carnivoreBreakfast.setFats(BigDecimal.valueOf(30));
        carnivoreBreakfast.setTotalCalories(BigDecimal.valueOf(320));
        carnivoreBreakfast.setDescription("A classic and simple breakfast consisting of crispy bacon and scrambled or fried eggs, providing plenty of protein and healthy fats.");
        carnivoreBreakfast.setName("Bacon and Eggs");
        carnivoreBreakfast.setAddedOn(LocalDate.now());
        carnivoreBreakfast.setPicture("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/-2019-07-30_Streaky_bacon%2C_fried_egg_on_toast%2C_Cromer_%281%29.JPG/250px--2019-07-30_Streaky_bacon%2C_fried_egg_on_toast%2C_Cromer_%281%29.JPG");
        carnivoreBreakfast.setMealCatalog(thirdCatalog);

        Meal carnivoreLunch = new Meal();
        carnivoreLunch.setOwner(owner);
        carnivoreLunch.setProteins(BigDecimal.valueOf(50));
        carnivoreLunch.setCarbs(BigDecimal.valueOf(0));
        carnivoreLunch.setFats(BigDecimal.valueOf(35));
        carnivoreLunch.setTotalCalories(BigDecimal.valueOf(480));
        carnivoreLunch.setDescription("A satisfying lunch featuring a grilled ribeye steak, topped with a pat of butter for extra flavor and fats.");
        carnivoreLunch.setName("Grilled Steak with Butter");
        carnivoreLunch.setAddedOn(LocalDate.now());
        carnivoreLunch.setPicture("https://thestayathomechef.com/wp-content/uploads/2024/05/How-to-Grill-Steaks-Perfectly-Every-Time-17.jpg");
        carnivoreLunch.setMealCatalog(thirdCatalog);

        Meal carnivoreDinner = new Meal();
        carnivoreDinner.setOwner(owner);
        carnivoreDinner.setProteins(BigDecimal.valueOf(40));
        carnivoreDinner.setCarbs(BigDecimal.valueOf(0));
        carnivoreDinner.setFats(BigDecimal.valueOf(22));
        carnivoreDinner.setTotalCalories(BigDecimal.valueOf(336));
        carnivoreDinner.setDescription("A filling dinner with roasted chicken, including the skin for added fat, which is rich in protein and fat.");
        carnivoreDinner.setName("Roasted Chicken with Skin");
        carnivoreDinner.setAddedOn(LocalDate.now());
        carnivoreDinner.setPicture("https://www.gimmesomeoven.com/wp-content/uploads/2009/12/garlic-roasted-chicken2.jpg");
        carnivoreDinner.setMealCatalog(thirdCatalog);

        thirdCatalog.getMeals().add(carnivoreBreakfast);
        thirdCatalog.getMeals().add(carnivoreLunch);
        thirdCatalog.getMeals().add(carnivoreDinner);
        mealCatalogRepository.save(thirdCatalog);
    }
}
