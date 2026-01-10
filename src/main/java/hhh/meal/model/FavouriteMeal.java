package hhh.meal.model;

import hhh.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "favourite_meals")
public class FavouriteMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Meal meal;

    @Column(nullable = false)
    private LocalDate favouritedOn;

    // No-args constructor (required by JPA)
    public FavouriteMeal() {
    }

    // All-args constructor
    public FavouriteMeal(UUID id, User user, Meal meal, LocalDate favouritedOn) {
        this.id = id;
        this.user = user;
        this.meal = meal;
        this.favouritedOn = favouritedOn;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public LocalDate getFavouritedOn() {
        return favouritedOn;
    }

    public void setFavouritedOn(LocalDate favouritedOn) {
        this.favouritedOn = favouritedOn;
    }
}
