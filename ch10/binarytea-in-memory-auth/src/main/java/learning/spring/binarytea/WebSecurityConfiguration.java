package learning.spring.binarytea;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean("/login")
    public UrlFilenameViewController loginController() {
        UrlFilenameViewController controller = new UrlFilenameViewController();
        controller.setSupportedMethods(HttpMethod.GET.name());
        controller.setSuffix(".html");
        return controller;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .mvcMatchers("/actuator/*").permitAll()
                    .anyRequest().authenticated().and()
                .formLogin() // 使用表单登录
                    .loginPage("/login").permitAll() // 设置登录页地址，全员可访问
                    .defaultSuccessUrl("/order")
                    .failureUrl("/login")
                    .loginProcessingUrl("/doLogin")
                    .usernameParameter("user")
                    .passwordParameter("pwd").and()
                .httpBasic(); // 使用HTTP Basic认证
    }

    @Bean
    public UserDetailsService userDetailsService(ObjectProvider<PasswordEncoder> passwordEncoder) {
        PasswordEncoder encoder = passwordEncoder
                .getIfAvailable(() -> PasswordEncoderFactories.createDelegatingPasswordEncoder());
        UserDetails employee = User.builder()
                .username("lilei")
                .password("binarytea")
                .authorities("READ_ORDER", "WRITE_ORDER")
                .passwordEncoder(encoder::encode)
                .build();
        return new InMemoryUserDetailsManager(employee);
    }
}
