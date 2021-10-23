package learning.spring.binarytea.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@DubboService
//@Component("menuServiceDubboImpl")
public class MenuServiceDubboImpl implements MenuService {
    @Autowired
    private learning.spring.binarytea.service.MenuService menuService;

    @Override
    public List<MenuItem> getAllMenu() {
        return menuService.getAllMenu().stream().map(i -> convert(i)).collect(Collectors.toList());
    }

    private MenuItem convert(learning.spring.binarytea.model.MenuItem origin) {
        MenuItem item = new MenuItem();
        BeanUtils.copyProperties(origin, item);
        item.setSize(origin.getSize().name());
        item.setPrice(origin.getPrice().getAmountMinorLong());
        return item;
    }
}
