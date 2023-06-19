package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndUniversity;

import java.util.List;

public interface UserAndUniversityRepository extends JpaRepository<UserAndUniversity, Long> {
    List<UserAndUniversity> findAllByUserId(Long userId);
}
