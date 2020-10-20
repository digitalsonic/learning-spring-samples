package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.RedisMenuItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisMenuRepository extends CrudRepository<RedisMenuItem, Long> {
    List<RedisMenuItem> findByName(String name);
}
