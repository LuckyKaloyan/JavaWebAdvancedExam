package hhh.meal.repository;
import hhh.meal.model.Meal;
import hhh.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {
    List<Meal> findByAddedOnAfter(LocalDate oneMonthAgo);
}
