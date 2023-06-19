package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndKeyword;

import java.util.List;

public interface UserAndKeywordRepository extends JpaRepository<UserAndKeyword, Long> {
    List<UserAndKeyword> findAllByUserId(Long userId);
}
