package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.CompanySize;

public interface CompanySizeRepository extends JpaRepository<CompanySize, Long> {
}
