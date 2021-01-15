package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByNameAndSize(String name, Size size);
}
