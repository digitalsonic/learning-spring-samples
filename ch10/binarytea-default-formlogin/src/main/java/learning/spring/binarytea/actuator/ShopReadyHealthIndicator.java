package learning.spring.binarytea.actuator;

import learning.spring.binarytea.BinaryTeaProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class ShopReadyHealthIndicator extends AbstractHealthIndicator {
    private BinaryTeaProperties binaryTeaProperties;

    public ShopReadyHealthIndicator(
            ObjectProvider<BinaryTeaProperties> binaryTeaProperties) {
        this.binaryTeaProperties = binaryTeaProperties.getIfAvailable();
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        if (binaryTeaProperties == null || !binaryTeaProperties.isReady()) {
            builder.down();
        } else {
            builder.up();
        }
    }
}
