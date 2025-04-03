package hhh.comment.model;
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
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String text;
    private LocalDate createdOn;
    @ManyToOne
    private User user;
    @ManyToOne
    private Meal meal;

}
