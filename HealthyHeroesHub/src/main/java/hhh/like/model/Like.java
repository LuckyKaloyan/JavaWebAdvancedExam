package hhh.like.model;
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
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private LocalDate date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Meal meal;


}
