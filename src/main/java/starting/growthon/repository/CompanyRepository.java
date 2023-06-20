package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Company;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByNameContaining(String name);
}
