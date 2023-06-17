package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndCompany;

public interface UserAndCompanyRepository extends JpaRepository<UserAndCompany, Long> {
}
