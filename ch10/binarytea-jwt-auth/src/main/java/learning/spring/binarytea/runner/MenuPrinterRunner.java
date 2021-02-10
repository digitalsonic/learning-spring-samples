package learning.spring.binarytea.runner;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
@Slf4j
public class MenuPrinterRunner implements ApplicationRunner {
    @Autowired
    private MenuService menuService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("再次加载菜单列表，应该不会访问数据库");
        List<MenuItem> list = menuService.getAllMenu();
        log.info("共有{}个饮品可选。", list.size());
        list.forEach(i -> log.info("重新查询菜单项({})：{}", i.getId(),
                menuService.getByNameAndSize(i.getName(), i.getSize())));
    }
}
