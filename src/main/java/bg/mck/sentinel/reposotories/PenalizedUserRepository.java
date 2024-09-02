package bg.mck.sentinel.reposotories;

import bg.mck.sentinel.entities.PenalizedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenalizedUserRepository extends JpaRepository<PenalizedUser, Long> {

    Optional<PenalizedUser> findByUserId(String userId);
}
