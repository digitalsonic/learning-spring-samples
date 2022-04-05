package learning.spring.binarytea.support.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogHandlerInterceptor implements HandlerInterceptor {
    private ThreadLocal<LogDetails> logDetails = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        LogDetails details = new LogDetails();
        logDetails.set(details);
        details.setStartTime(System.currentTimeMillis());
        details.setRemoteAddr(StringUtils.defaultIfBlank(request.getRemoteAddr(), "-"));
        details.setUri(StringUtils.defaultIfBlank(request.getRequestURI(), "-"));
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            details.setHandler(hm.getBeanType().getSimpleName() + "." + hm.getMethod().getName() + "()");
        } else {
            details.setHandler(handler.getClass().getSimpleName());
        }
        details.setMethod(request.getMethod());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        LogDetails details = logDetails.get();
        if (details != null) {
            details.setProcessTime(System.currentTimeMillis());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        LogDetails details = logDetails.get();
        if (details != null) {
            details.setEndTime(System.currentTimeMillis());
            details.setException(ex == null ? "-" : ex.getClass().getSimpleName());
            details.setCode(response.getStatus());
            details.setUser("-");
            if (isAuthenticated() && isUser()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                details.setUser(StringUtils.defaultIfBlank(user.getUsername(), "-"));
            }
            printLog(details);
        }
        logDetails.remove();
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    private boolean isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User;
    }

    private void printLog(LogDetails details) {
        log.info("{},{},{},{},{},{},{},{}ms,{}ms",
                details.getRemoteAddr(), details.getMethod(), details.getUri(),
                details.getHandler(), details.getCode(), details.getException(), details.getUser(),
                details.getEndTime() - details.getStartTime(), // 总时间
                details.getProcessTime() - details.getStartTime()); // 处理时间
    }
}
