package mmm.winner.repository;

import mmm.winner.model.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WinnerRepository extends JpaRepository<Winner, UUID> {
    Optional<Winner> findFirstBy();
}
