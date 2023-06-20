package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
