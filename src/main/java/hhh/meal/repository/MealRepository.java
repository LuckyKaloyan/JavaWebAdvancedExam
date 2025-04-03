package hhh.meal.repository;
import hhh.meal.model.Meal;
import hhh.mealcatalog.model.MealCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {
    List<Meal> findByAddedOnAfter(LocalDate oneMonthAgo);
    List<Meal> findTop20ByOrderByUpVotesDesc();
    List<Meal> findByMealCatalog(MealCatalog mealCatalog);
}
