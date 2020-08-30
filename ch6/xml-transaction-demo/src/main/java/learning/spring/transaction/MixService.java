package learning.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MixService {
    @Autowired
    private DemoService demoService;

    public void trySomeMethods() {
        demoService.insertRecordRequired();
//        demoService.insertRecordRequiresNew();
        try {
            demoService.insertRecordNested();
        } catch(Exception e) {}
//        throw new RuntimeException();
    }
}
