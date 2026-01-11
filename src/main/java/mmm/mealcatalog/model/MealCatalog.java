package mmm.mealcatalog.model;

import mmm.meal.model.Meal;
import mmm.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meal_catalogs")
public class MealCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "added_on", nullable = false)
    private LocalDate addedOn;

    @Column(nullable = false)
    private MealCatalogType type;

    @ManyToOne
    private User owner;

    @OneToMany(
            mappedBy = "mealCatalog",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Meal> meals;

    // No-args constructor (required by JPA)
    public MealCatalog() {
    }

    // All-args constructor
    public MealCatalog(
            UUID id,
            String name,
            String description,
            LocalDate addedOn,
            MealCatalogType type,
            User owner,
            List<Meal> meals
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.addedOn = addedOn;
        this.type = type;
        this.owner = owner;
        this.meals = meals;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public MealCatalogType getType() {
        return type;
    }

    public void setType(MealCatalogType type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
