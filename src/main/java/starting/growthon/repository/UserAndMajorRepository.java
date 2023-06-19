package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndMajor;

import java.util.List;

public interface UserAndMajorRepository extends JpaRepository<UserAndMajor, Long> {
    List<UserAndMajor> findAllByUserId(Long userId);
}
