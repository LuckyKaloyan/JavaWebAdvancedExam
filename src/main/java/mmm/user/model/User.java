package mmm.user.model;

import mmm.comment.model.Comment;
import mmm.meal.model.FavouriteMeal;
import mmm.mealcatalog.model.MealCatalog;
import mmm.report.model.Report;
import mmm.upvote.model.UpVote;
import mmm.winner.model.Winner;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    private String firstName;
    private String lastName;

    @Column(length = 1000)
    private String profilePicture;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private double dailyCalories;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealCatalog> mealCatalogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavouriteMeal> favouriteMeals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpVote> upVotes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "concernedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @OneToOne(mappedBy = "user")
    private Winner winner;

    // No-args constructor (required by JPA)
    public User() {
    }

    // All-args constructor
    public User(
            UUID id,
            String username,
            String password,
            String email,
            String phone,
            String firstName,
            String lastName,
            String profilePicture,
            LocalDate registrationDate,
            UserRole role,
            double dailyCalories,
            List<MealCatalog> mealCatalogs,
            List<FavouriteMeal> favouriteMeals,
            List<UpVote> upVotes,
            List<Comment> comments,
            List<Report> reports,
            Winner winner
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.registrationDate = registrationDate;
        this.role = role;
        this.dailyCalories = dailyCalories;
        this.mealCatalogs = mealCatalogs;
        this.favouriteMeals = favouriteMeals;
        this.upVotes = upVotes;
        this.comments = comments;
        this.reports = reports;
        this.winner = winner;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public double getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(double dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public List<MealCatalog> getMealCatalogs() {
        return mealCatalogs;
    }

    public void setMealCatalogs(List<MealCatalog> mealCatalogs) {
        this.mealCatalogs = mealCatalogs;
    }

    public List<FavouriteMeal> getFavouriteMeals() {
        return favouriteMeals;
    }

    public void setFavouriteMeals(List<FavouriteMeal> favouriteMeals) {
        this.favouriteMeals = favouriteMeals;
    }

    public List<UpVote> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(List<UpVote> upVotes) {
        this.upVotes = upVotes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }
}
