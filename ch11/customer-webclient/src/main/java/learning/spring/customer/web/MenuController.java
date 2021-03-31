package learning.spring.customer.web;

import learning.spring.customer.integration.MenuService;
import learning.spring.customer.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping
    public Flux<MenuItem> getAllMenu() {
        return menuService.getAllMenu();
    }

    @GetMapping(path = "/{id}")
    public Mono<MenuItem> getById(@PathVariable Long id) {
        if (id == null) {
            return Mono.empty();
        }
        return menuService.getById(id);
    }
}
