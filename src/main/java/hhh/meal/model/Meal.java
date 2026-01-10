package hhh.meal.model;

import hhh.comment.model.Comment;
import hhh.mealcatalog.model.MealCatalog;
import hhh.upvote.model.UpVote;
import hhh.user.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal proteins;

    @Column(nullable = false)
    private BigDecimal carbs;

    @Column(nullable = false)
    private BigDecimal fats;

    @Column(name = "total_calories", nullable = false)
    private BigDecimal totalCalories;

    @Column(name = "added_on", nullable = false)
    private LocalDate addedOn;

    @Column(length = 1000, nullable = false)
    private String picture;

    @ManyToOne
    private User owner;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<UpVote> upVotes;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FavouriteMeal> favouriteMeals;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;

    @ManyToOne
    private MealCatalog mealCatalog;

    // No-args constructor (required by JPA)
    public Meal() {
    }

    // All-args constructor
    public Meal(
            UUID id,
            String name,
            String description,
            BigDecimal proteins,
            BigDecimal carbs,
            BigDecimal fats,
            BigDecimal totalCalories,
            LocalDate addedOn,
            String picture,
            User owner,
            List<UpVote> upVotes,
            List<FavouriteMeal> favouriteMeals,
            List<Comment> comments,
            MealCatalog mealCatalog
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fats = fats;
        this.totalCalories = totalCalories;
        this.addedOn = addedOn;
        this.picture = picture;
        this.owner = owner;
        this.upVotes = upVotes;
        this.favouriteMeals = favouriteMeals;
        this.comments = comments;
        this.mealCatalog = mealCatalog;
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

    public BigDecimal getProteins() {
        return proteins;
    }

    public void setProteins(BigDecimal proteins) {
        this.proteins = proteins;
    }

    public BigDecimal getCarbs() {
        return carbs;
    }

    public void setCarbs(BigDecimal carbs) {
        this.carbs = carbs;
    }

    public BigDecimal getFats() {
        return fats;
    }

    public void setFats(BigDecimal fats) {
        this.fats = fats;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(BigDecimal totalCalories) {
        this.totalCalories = totalCalories;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<UpVote> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(List<UpVote> upVotes) {
        this.upVotes = upVotes;
    }

    public List<FavouriteMeal> getFavouriteMeals() {
        return favouriteMeals;
    }

    public void setFavouriteMeals(List<FavouriteMeal> favouriteMeals) {
        this.favouriteMeals = favouriteMeals;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public MealCatalog getMealCatalog() {
        return mealCatalog;
    }

    public void setMealCatalog(MealCatalog mealCatalog) {
        this.mealCatalog = mealCatalog;
    }
}
