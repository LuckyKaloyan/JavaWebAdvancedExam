package hhh.winner.model;

import hhh.meal.model.Meal;
import hhh.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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


}
