package hhh.meal.repository;


import hhh.meal.model.FavouriteMeal;
import hhh.meal.model.Meal;
import hhh.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavouriteMealRepository extends JpaRepository<FavouriteMeal, UUID> {
    Optional<FavouriteMeal> findByUserAndMeal(User user, Meal meal);


}
