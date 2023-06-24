package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Year;

public interface YearRepository extends JpaRepository<Year, Long> {
    Year findByName(String name);
}
