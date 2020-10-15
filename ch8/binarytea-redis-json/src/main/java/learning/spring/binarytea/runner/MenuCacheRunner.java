package learning.spring.binarytea.runner;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Order(1)
public class MenuCacheRunner implements ApplicationRunner {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MenuItem> itemList = menuRepository.findAll();
        log.info("Load {} MenuItems from DB, ready to cache.", itemList.size());
        redisTemplate.opsForList().leftPushAll("binarytea-menu", itemList);
        redisTemplate.expire("binarytea-menu", 300, TimeUnit.SECONDS);
    }
}
