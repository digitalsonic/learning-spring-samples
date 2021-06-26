package learning.spring.binarytea.support.ready;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("learning.spring.binarytea.controller")
@Slf4j
public class ReadyStateControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleException(ShopNotReadyException e) {
        ResponseEntity<Map<String, String>> entity = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("message",
                        "Binarytea is NOT ready yet. Please come later."));
        log.warn("NOT ready yet! Can NOT accept requests.");
        return entity;
    }
}
