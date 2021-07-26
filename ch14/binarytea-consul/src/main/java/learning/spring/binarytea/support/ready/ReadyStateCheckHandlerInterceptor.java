package learning.spring.binarytea.support.ready;

import learning.spring.binarytea.BinaryTeaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class ReadyStateCheckHandlerInterceptor implements HandlerInterceptor {
    @Autowired(required = false)
    private Optional<BinaryTeaProperties> binaryTeaProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (!MediaType.TEXT_HTML_VALUE.equalsIgnoreCase(request.getHeader(HttpHeaders.ACCEPT))) {
            boolean isReady = binaryTeaProperties.orElse(new BinaryTeaProperties()).isReady();
            if (!isReady) {
                log.debug("Shop is NOT ready!");
                throw new ShopNotReadyException("NOT Ready");
            }
        }
        log.debug("Shop is ready, continue.");
        return true;
    }
}
