package hhh.upvote.repository;
import hhh.meal.model.Meal;
import hhh.upvote.model.UpVote;
import hhh.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UpVoteRepository extends JpaRepository<UpVote, UUID> {
    Optional<UpVote> findByMealAndUser(Meal meal, User user);
    List<UpVote> findByDateAfter(LocalDate oneYearAgo);
}
