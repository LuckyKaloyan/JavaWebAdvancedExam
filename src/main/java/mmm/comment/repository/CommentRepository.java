package mmm.comment.repository;
import mmm.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByCreatedOnAfter(LocalDate oneYearAgo);
}
