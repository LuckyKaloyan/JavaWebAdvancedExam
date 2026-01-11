package mmm.mealcatalog.repository;
import mmm.mealcatalog.model.MealCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface MealCatalogRepository extends JpaRepository<MealCatalog, UUID> {

    Optional<MealCatalog> getMealCatalogById(UUID id);
}
