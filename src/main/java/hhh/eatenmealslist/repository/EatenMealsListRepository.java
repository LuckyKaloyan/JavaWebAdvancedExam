package hhh.eatenmealslist.repository;

import hhh.eatenmealslist.model.EatenMealsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EatenMealsListRepository extends JpaRepository<EatenMealsList,UUID> {
    Optional<EatenMealsList> findByUserId(UUID userId);
}
