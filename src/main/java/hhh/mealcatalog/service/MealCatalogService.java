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
    public MealCatalogService(MealCatalogRepository mealCatalogRepository, UserService userService, EatenMealsListService eatenMealsListService) {
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

        for (Meal meal : mealCatalogRepository.getMealCatalogById(mealCatalogId).get().getMeals()) {
            eatenMealsListService.deleteAllMealsWithId(meal.getId());
        }

        mealCatalogRepository.deleteById(mealCatalogId);
    }

    @Transactional
    public void createDefaultCatalog() {
        User owner = userService.getByUsername("luckykaloyan");

        MealCatalog firstCatalog = MealCatalog.builder()
                .owner(owner)
                .description("This exclusive omnivore meal catalog showcases a curated selection of the finest mixed dishes, thoughtfully compiled and presented by the admin!")
                .name("LuckyKaloyan's omnivore meal catalog.")
                .addedOn(LocalDate.now())
                .meals(new ArrayList<>())
                .type(MealCatalogType.OMNIVORE)
                .build();

        Meal omnivoreBreakfast = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(5))
                .carbs(BigDecimal.valueOf(15))
                .fats(BigDecimal.valueOf(4))
                .totalCalories(BigDecimal.valueOf(112))
                .description("A healthy and filling dish combining oats and yogurt, rich in fiber and protein.")
                .name("Oats with yogurt(per 100 gr.).")
                .addedOn(LocalDate.now())
                .picture("https://www.luvele.co.uk/cdn/shop/articles/oat_milk_yogurt_06_1024x.png?v=1718168524")
                .mealCatalog(firstCatalog)
                .build();

        Meal omnivoreLunch= Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(40))
                .carbs(BigDecimal.valueOf(40))
                .fats(BigDecimal.valueOf(25))
                .totalCalories(BigDecimal.valueOf(420))
                .description("A nutritious and balanced meal with spinach, eggs, pork, butter, and mixed vegetables, providing protein, healthy fats, and essential vitamins.")
                .name("Spinach, Eggs, and Pork Lunch(per 450 gr.).")
                .addedOn(LocalDate.now())
                .picture("https://thumb.photo-ac.com/9c/9cd109f8ae164a3b13f5e0a40824611f_t.jpeg")
                .mealCatalog(firstCatalog)
                .build();

        Meal omnivoreDinner = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(35))
                .carbs(BigDecimal.valueOf(10))
                .fats(BigDecimal.valueOf(22))
                .totalCalories(BigDecimal.valueOf(356))
                .description("A healthy and satisfying meal featuring grilled salmon and a variety of roasted or steamed vegetables, rich in omega-3 fatty acids, protein, and essential vitamins.")
                .name("Salmon with Vegetables Dinner(per 250 gr.).")
                .addedOn(LocalDate.now())
                .picture("https://www.theroastedroot.net/wp-content/uploads/2020/02/one_pan_salmon_and_vegetables_1.jpg")
                .mealCatalog(firstCatalog)
                .build();

        firstCatalog.getMeals().add(omnivoreBreakfast);
        firstCatalog.getMeals().add(omnivoreLunch);
        firstCatalog.getMeals().add(omnivoreDinner);
        mealCatalogRepository.save(firstCatalog);

        MealCatalog secondCatalog = MealCatalog.builder()
                .owner(owner)
                .description("This exclusive meal catalog showcases a curated selection of the finest vegan dishes, thoughtfully compiled and presented by the admin!")
                .name("LuckyKaloyan's vegan meal catalog.")
                .addedOn(LocalDate.now())
                .meals(new ArrayList<>())
                .type(MealCatalogType.VEGAN)
                .build();

        Meal veganBreakfast = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(6))
                .carbs(BigDecimal.valueOf(25))
                .fats(BigDecimal.valueOf(15))
                .totalCalories(BigDecimal.valueOf(244))
                .description("A creamy and nutrient-packed breakfast made with chia seeds, almond milk, and topped with fresh berries for added flavor and antioxidants.")
                .name("Chia Pudding with Berries(per 100 gr.).")
                .addedOn(LocalDate.now())
                .picture("https://www.thehealthymaven.com/wp-content/uploads/2016/03/Verry-Berry-Chia-Pudding-6.png")
                .mealCatalog(secondCatalog)
                .build();

        Meal veganLunch = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(12))
                .carbs(BigDecimal.valueOf(40))
                .fats(BigDecimal.valueOf(18))
                .totalCalories(BigDecimal.valueOf(352))
                .description("A light yet filling salad with quinoa, chickpeas, avocado, cucumber, and a lemon-tahini dressing, packed with protein, healthy fats, and fiber.")
                .name("Quinoa Salad with Chickpeas and Avocado")
                .addedOn(LocalDate.now())
                .picture("https://www.eatingwell.com/thmb/lYBxQk2CC-BPJpnHO3foW4KjWes=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/6859253-83bb7ca335f046d694de9aa8042eaf0a.jpg")
                .mealCatalog(secondCatalog)
                .build();

        Meal veganDinner = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(18))
                .carbs(BigDecimal.valueOf(40))
                .fats(BigDecimal.valueOf(10))
                .totalCalories(BigDecimal.valueOf(312))
                .description("A savory stir-fry made with lentils, mixed vegetables (such as bell peppers, carrots, and spinach), and a soy-ginger sauce for a delicious, protein-rich meal.")
                .name("Lentil and Vegetable Stir-Fry")
                .addedOn(LocalDate.now())
                .picture("https://www.theroastedroot.net/wp-content/uploads/2020/02/one_pan_salmon_and_vegetables_1.jpg")
                .mealCatalog(secondCatalog)
                .build();

        secondCatalog.getMeals().add(veganBreakfast);
        secondCatalog.getMeals().add(veganLunch);
        secondCatalog.getMeals().add(veganDinner);
        mealCatalogRepository.save(secondCatalog);

        MealCatalog thirdCatalog = MealCatalog.builder()
                .owner(owner)
                .description("This exclusive meal catalog showcases a curated selection of the finest carnivore dishes, thoughtfully compiled and presented by the admin!")
                .name("LuckyKaloyan's carnivore meal catalog.")
                .addedOn(LocalDate.now())
                .meals(new ArrayList<>())
                .type(MealCatalogType.CARNIVORE)
                .build();

        Meal carnivoreBreakfast = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(20))
                .carbs(BigDecimal.valueOf(0))
                .fats(BigDecimal.valueOf(30))
                .totalCalories(BigDecimal.valueOf(320))
                .description("A classic and simple breakfast consisting of crispy bacon and scrambled or fried eggs, providing plenty of protein and healthy fats.")
                .name("Bacon and Eggs")
                .addedOn(LocalDate.now())
                .picture("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/-2019-07-30_Streaky_bacon%2C_fried_egg_on_toast%2C_Cromer_%281%29.JPG/250px--2019-07-30_Streaky_bacon%2C_fried_egg_on_toast%2C_Cromer_%281%29.JPG")
                .mealCatalog(thirdCatalog)
                .build();

        Meal carnivoreLunch = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(50))
                .carbs(BigDecimal.valueOf(0))
                .fats(BigDecimal.valueOf(35))
                .totalCalories(BigDecimal.valueOf(480))
                .description("A satisfying lunch featuring a grilled ribeye steak, topped with a pat of butter for extra flavor and fats.")
                .name("Grilled Steak with Butter")
                .addedOn(LocalDate.now())
                .picture("https://thestayathomechef.com/wp-content/uploads/2024/05/How-to-Grill-Steaks-Perfectly-Every-Time-17.jpg")
                .mealCatalog(thirdCatalog)
                .build();

        Meal carnivoreDinner = Meal.builder()
                .owner(owner)
                .proteins(BigDecimal.valueOf(40))
                .carbs(BigDecimal.valueOf(0))
                .fats(BigDecimal.valueOf(22))
                .totalCalories(BigDecimal.valueOf(336))
                .description("A filling dinner with roasted chicken, including the skin for added fat, which is rich in protein and fat.")
                .name("Roasted Chicken with Skin")
                .addedOn(LocalDate.now())
                .picture("https://www.gimmesomeoven.com/wp-content/uploads/2009/12/garlic-roasted-chicken2.jpg")
                .mealCatalog(thirdCatalog)
                .build();

        thirdCatalog.getMeals().add(carnivoreBreakfast);
        thirdCatalog.getMeals().add(carnivoreLunch);
        thirdCatalog.getMeals().add(carnivoreDinner);
        mealCatalogRepository.save(thirdCatalog);

    }
}
