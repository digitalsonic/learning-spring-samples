package learning.spring.binarytea.actuator;

import learning.spring.binarytea.BinaryTeaProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "shop")
public class ShopEndpoint {
    private BinaryTeaProperties binaryTeaProperties;

    public ShopEndpoint(
            ObjectProvider<BinaryTeaProperties> binaryTeaProperties) {
        this.binaryTeaProperties = binaryTeaProperties.getIfAvailable();
    }

    @ReadOperation
    public String state() {
        if (binaryTeaProperties == null || !binaryTeaProperties.isReady()) {
            return "We're not ready.";
        } else {
            return "We open " + binaryTeaProperties.getOpenHours() + ".";
        }
    }
}
