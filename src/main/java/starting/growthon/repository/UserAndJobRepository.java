package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndJob;

import java.util.List;

public interface UserAndJobRepository extends JpaRepository<UserAndJob, Long> {
    List<UserAndJob> findAllByUserId(Long userId);
}
