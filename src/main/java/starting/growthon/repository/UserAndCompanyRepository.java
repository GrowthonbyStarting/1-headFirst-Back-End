package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndCompany;

import java.util.List;

public interface UserAndCompanyRepository extends JpaRepository<UserAndCompany, Long> {
    List<UserAndCompany> findAllByUserId(Long userId);
}
