package hhh.mealcatalog.repository;
import hhh.mealcatalog.model.MealCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface MealCatalogRepository extends JpaRepository<MealCatalog, UUID> {
}
