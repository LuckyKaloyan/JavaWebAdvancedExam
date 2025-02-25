package hhh.comment.repository;
import hhh.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
