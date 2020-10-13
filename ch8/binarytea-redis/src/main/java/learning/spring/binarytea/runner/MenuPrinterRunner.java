package learning.spring.binarytea.runner;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Order(2)
public class MenuPrinterRunner implements ApplicationRunner {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long size = 0;
        List<MenuItem> menuItemList = null;
        if (redisTemplate.hasKey("binarytea-menu")) {
            BoundListOperations<String, MenuItem> operations = redisTemplate.boundListOps("binarytea-menu");
            size = operations.size();
            menuItemList = operations.range(0, -1);
            log.info("Loading menu from Redis.");
        } else {
            size = menuRepository.count();
            menuItemList = menuRepository.findAll();
            log.info("Loading menu from DB.");
        }
        log.info("共有{}个饮品可选。", size);
        menuItemList.forEach(i -> log.info("饮品：{}", i));
    }
}
