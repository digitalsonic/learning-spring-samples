package learning.spring.teamaker.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import learning.spring.teamaker.model.ProcessResult;
import learning.spring.teamaker.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/{id}")
    public ResponseEntity<ProcessResult> process(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        ProcessResult result = orderService.make(id);
        log.info("成功完成订单{}的制作", id);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ProcessResult> handleRequestNotPermitted(HttpServletRequest request) {
        log.warn("请求{}触发限流", request.getRequestURI());
        ProcessResult result = ProcessResult.builder().finish(false).build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
    }
}
