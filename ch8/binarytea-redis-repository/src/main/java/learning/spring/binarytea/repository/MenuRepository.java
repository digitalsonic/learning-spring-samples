package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuItem, Long> {
}
