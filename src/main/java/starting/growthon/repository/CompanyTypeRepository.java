package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.CompanyType;

public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {
    CompanyType findByName(String name);
}
