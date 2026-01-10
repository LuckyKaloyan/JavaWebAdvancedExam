package hhh.winner.model;

import hhh.meal.model.Meal;
import hhh.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Winner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int totalCatalogs;
    private int totalMeals;
    private int totalUpVotes;
    private int totalFavourites;
    private int totalComments;
    private LocalDate crowningDate;

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(optional = true)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    // No-args constructor
    public Winner() {
    }

    // All-args constructor
    public Winner(
            UUID id,
            int totalCatalogs,
            int totalMeals,
            int totalUpVotes,
            int totalFavourites,
            int totalComments,
            LocalDate crowningDate,
            User user,
            Meal meal
    ) {
        this.id = id;
        this.totalCatalogs = totalCatalogs;
        this.totalMeals = totalMeals;
        this.totalUpVotes = totalUpVotes;
        this.totalFavourites = totalFavourites;
        this.totalComments = totalComments;
        this.crowningDate = crowningDate;
        this.user = user;
        this.meal = meal;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTotalCatalogs() {
        return totalCatalogs;
    }

    public void setTotalCatalogs(int totalCatalogs) {
        this.totalCatalogs = totalCatalogs;
    }

    public int getTotalMeals() {
        return totalMeals;
    }

    public void setTotalMeals(int totalMeals) {
        this.totalMeals = totalMeals;
    }

    public int getTotalUpVotes() {
        return totalUpVotes;
    }

    public void setTotalUpVotes(int totalUpVotes) {
        this.totalUpVotes = totalUpVotes;
    }

    public int getTotalFavourites() {
        return totalFavourites;
    }

    public void setTotalFavourites(int totalFavourites) {
        this.totalFavourites = totalFavourites;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public LocalDate getCrowningDate() {
        return crowningDate;
    }

    public void setCrowningDate(LocalDate crowningDate) {
        this.crowningDate = crowningDate;
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
}
