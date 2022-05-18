package learning.spring.binarytea.service;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Size;
import learning.spring.binarytea.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@CacheConfig(cacheNames = "menu")
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @PreAuthorize("hasAuthority('WRITE_MENU')")
    public Optional<MenuItem> save(MenuItem menuItem) {
        return Optional.ofNullable(menuRepository.save(menuItem));
    }

    @PreAuthorize("hasAuthority('WRITE_MENU')")
    public List<MenuItem> save(List<MenuItem> items) {
        return menuRepository.saveAll(items);
    }

    @Cacheable
    public List<MenuItem> getAllMenu() {
        return menuRepository.findAll();
    }

    public Optional<MenuItem> getById(Long id) {
        return menuRepository.findById(id);
    }

    public List<MenuItem> getByName(String name) {
        return menuRepository.findAll(Example.of(MenuItem.builder().name(name).build()), Sort.by("id"));
    }

    public List<MenuItem> getByIdList(List<Long> idList) {
        return menuRepository.findAllById(idList);
    }

//    @Cacheable(key = "#root.methodName + '-' + #name + '-' + #size")
    public Optional<MenuItem> getByNameAndSize(String name, Size size) {
        return menuRepository.findByNameAndSize(name, size);
    }
}
