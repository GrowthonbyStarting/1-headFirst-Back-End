package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndUniversity;

public interface UserAndUniversityRepository extends JpaRepository<UserAndUniversity, Long> {
}
