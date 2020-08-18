package learning.spring.binarytea;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("binarytea")
public class BinaryTeaProperties {
    private boolean ready;
    private String openHours;

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }
}
