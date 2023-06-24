package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.CompanyType;

public interface CompanySizeRepository extends JpaRepository<CompanyType, Long> {
}
