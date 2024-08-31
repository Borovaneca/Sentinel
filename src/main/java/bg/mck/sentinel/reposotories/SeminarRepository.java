package bg.mck.sentinel.reposotories;

import bg.mck.sentinel.entities.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeminarRepository extends JpaRepository<Seminar, Long> {

    Seminar findFirstByOrderByIdDesc();

    List<Seminar> findTop5ByOrderByIdDesc();
}
