package learning.spring.binarytea.support.log;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogDetails {
    private long startTime; // 开始处理的时间
    private long processTime; // 处理的结束时间
    private long endTime; // 完成视图呈现的时间
    private int code; // 返回的HTTP响应码
    private String handler; // 具体的处理器
    private String method; // 请求的HTTP方法
    private String uri; // 请求的URI
    private String remoteAddr; // 发起请求的对端地址
    private String exception; // 发生的异常类
    private String user; // 登录的用户信息
}
