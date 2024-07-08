package bg.mck.sentinel.reposotories;

import bg.mck.sentinel.entities.GoodSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodSiteRepository extends JpaRepository<GoodSite, Long> {

    boolean existsByDomain(String domain);

    Optional<GoodSite> findByDomain(String domain);
}
