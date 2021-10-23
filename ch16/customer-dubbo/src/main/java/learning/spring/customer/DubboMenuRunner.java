package learning.spring.customer;

import learning.spring.binarytea.dubbo.MenuItem;
import learning.spring.binarytea.dubbo.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Order(3)
public class DubboMenuRunner implements ApplicationRunner {
    @DubboReference
    private MenuService menuService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MenuItem> items = menuService.getAllMenu();
        log.info("通过Dubbo接口获得了{}个菜单项", items.size());
    }
}
