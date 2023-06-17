package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Major;

public interface MajorRepository extends JpaRepository<Major, Long> {
}
