package learning.spring.binarytea.runner;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.RedisMenuItem;
import learning.spring.binarytea.repository.MenuRepository;
import learning.spring.binarytea.repository.RedisMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Order(1)
public class MenuCacheRunner implements ApplicationRunner {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RedisMenuRepository redisMenuRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MenuItem> itemList = menuRepository.findAll();
        log.info("Load {} MenuItems from DB, ready to cache.", itemList.size());

        itemList.forEach(i -> {
            RedisMenuItem rmi = new RedisMenuItem();
            BeanUtils.copyProperties(i, rmi);
            redisMenuRepository.save(rmi);
        });
    }
}
