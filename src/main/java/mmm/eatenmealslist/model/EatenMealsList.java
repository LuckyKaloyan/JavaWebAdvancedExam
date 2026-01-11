package mmm.eatenmealslist.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class EatenMealsList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @ElementCollection
    @Column
    private List<UUID> mealsIds = new ArrayList<>();

    // No-args constructor (required by JPA)
    public EatenMealsList() {
    }

    // All-args constructor
    public EatenMealsList(UUID id, UUID userId, List<UUID> mealsIds) {
        this.id = id;
        this.userId = userId;
        this.mealsIds = mealsIds;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<UUID> getMealsIds() {
        return mealsIds;
    }

    public void setMealsIds(List<UUID> mealsIds) {
        this.mealsIds = mealsIds;
    }
}
