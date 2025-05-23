package hhh.user.model;
import hhh.comment.model.Comment;
import hhh.meal.model.FavouriteMeal;
import hhh.mealcatalog.model.MealCatalog;
import hhh.report.model.Report;
import hhh.upvote.model.UpVote;
import hhh.winner.model.Winner;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}