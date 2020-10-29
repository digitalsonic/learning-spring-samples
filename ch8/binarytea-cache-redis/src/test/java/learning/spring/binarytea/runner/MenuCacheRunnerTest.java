package learning.spring.binarytea.runner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MenuCacheRunnerTest {
    @Autowired
    private CacheManager cacheManager;

    @Test
    void testCache() {
        Cache cache = cacheManager.getCache("menu");
        assertTrue(cacheManager instanceof RedisCacheManager);
        assertTrue(cache instanceof RedisCache);
        assertNotNull(cache.get("getByNameAndSize-Java咖啡-MEDIUM"));
    }
}