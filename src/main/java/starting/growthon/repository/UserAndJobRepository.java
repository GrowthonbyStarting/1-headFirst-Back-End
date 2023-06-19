package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndJob;

public interface UserAndJobRepository extends JpaRepository<UserAndJob, Long> {
}
