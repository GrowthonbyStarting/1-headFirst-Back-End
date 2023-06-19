package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndMajor;

public interface UserAndMajorRepository extends JpaRepository<UserAndMajor, Long> {
}
