package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUuid(Long uuid);
    List<User> findAllByCompanyId(Long companyId);
    List<User> findAllBySubjobId(Long subjobId);
}
