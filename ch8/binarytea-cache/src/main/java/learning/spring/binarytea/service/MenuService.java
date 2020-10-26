package learning.spring.binarytea.service;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Size;
import learning.spring.binarytea.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "menu")
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Cacheable
    public List<MenuItem> getAllMenu() {
        return menuRepository.findAll();
    }

    @Cacheable(key = "#root.methodName + '-' + #name + '-' + #size")
    public Optional<MenuItem> getByNameAndSize(String name, Size size) {
        return menuRepository.findByNameAndSize(name, size);
    }
}
