package bg.mck.sentinel.reposotories;

import bg.mck.sentinel.entities.SubDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDomainRepository extends JpaRepository<SubDomain, Long> {
    SubDomain getBySubDomain(String subDomain);
}
