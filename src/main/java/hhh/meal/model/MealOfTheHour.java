package hhh.meal.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MealOfTheHour {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int upVotes;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private UUID mealId;

}
