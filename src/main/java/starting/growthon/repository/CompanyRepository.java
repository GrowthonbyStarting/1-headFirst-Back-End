package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
