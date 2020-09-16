package learning.spring.binarytea.runner;

import learning.spring.binarytea.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuPrinterRunner implements ApplicationRunner {
    private final MenuRepository menuRepository;

    public MenuPrinterRunner(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("共有{}个饮品可选。", menuRepository.countMenuItems());
        menuRepository.queryAllItems()
                .forEach(i -> log.info("饮品：{}", i));
    }
}
