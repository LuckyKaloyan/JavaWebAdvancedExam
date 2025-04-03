package hhh.meal.repository;

import hhh.meal.model.MealOfTheHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MealOfTheHourRepository extends JpaRepository<MealOfTheHour, UUID> {
    MealOfTheHour findFirstBy();
}
