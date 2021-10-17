package learning.spring.binarytea.dubbo;

import learning.spring.binarytea.model.MenuItem;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
public class MenuServiceDubboImpl implements MenuService {
    private learning.spring.binarytea.service.MenuService menuService;

    @Override
    public List<MenuItem> getAllMenu() {
        return menuService.getAllMenu();
    }
}
