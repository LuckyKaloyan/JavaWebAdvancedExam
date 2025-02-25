package hhh.mealcatalog.model;
import hhh.meal.model.Meal;
import hhh.user.model.User;
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
public class MealCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate addedOn;
    @Column(nullable = false)
    private LocalDate lastUpdate;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "mealCatalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;




}
