package mmm.comment.model;

import mmm.meal.model.Meal;
import mmm.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

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

    public Comment() {
    }


    public Comment(UUID id, String text, LocalDate createdOn, User user, Meal meal) {
        this.id = id;
        this.text = text;
        this.createdOn = createdOn;
        this.user = user;
        this.meal = meal;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
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
