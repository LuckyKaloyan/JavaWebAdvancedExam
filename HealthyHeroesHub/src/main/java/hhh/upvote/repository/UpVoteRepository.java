package hhh.upvote.repository;
import hhh.upvote.model.UpVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UpVoteRepository extends JpaRepository<UpVote, UUID> {
}
