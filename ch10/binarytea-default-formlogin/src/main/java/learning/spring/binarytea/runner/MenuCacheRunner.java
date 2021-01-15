package learning.spring.binarytea.runner;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Size;
import learning.spring.binarytea.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@Slf4j
public class MenuCacheRunner implements ApplicationRunner {
    @Autowired
    private MenuService menuService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("从数据库加载菜单列表，后续应该就在缓存里了");
        List<MenuItem> list = menuService.getAllMenu();
        log.info("共取得{}个条目。", list.size());
        menuService.getByNameAndSize("Java咖啡", Size.MEDIUM)
                .ifPresent(m -> log.info("加载中杯Java咖啡，放入缓存，ID={}", m.getId()));
    }
}
