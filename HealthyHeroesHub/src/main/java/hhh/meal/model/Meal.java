package hhh.meal.model;
import hhh.comment.model.Comment;
import hhh.mealcatalog.model.MealCatalog;
import hhh.upvote.model.UpVote;
import hhh.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    @Column(nullable = false)
    private BigDecimal totalCalories;
    @Column(nullable = false)
    private LocalDate addedOn;
    @Column(nullable = false)
    private String picture;
    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpVote> upVotes;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavouriteMeal> favouriteMeals;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    private MealCatalog mealCatalog;


}
