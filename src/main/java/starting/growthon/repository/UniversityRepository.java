package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.University;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
