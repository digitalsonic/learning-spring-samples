package learning.spring.binarytea;

import learning.spring.binarytea.support.jwt.JwtAuthenticationFilter;
import learning.spring.binarytea.support.jwt.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    private PreAuthenticatedAuthenticationProvider jwtPreAuthenticatedAuthenticationProvider;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setJwtTokenHelper(jwtTokenHelper);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAt(jwtAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML))
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON)).and()
                .anonymous()
                    .key("binarytea_anonymous").and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .mvcMatchers("/actuator/*").permitAll()
                    .mvcMatchers(HttpMethod.POST, "/token").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/menu", "/menu/**")
                        .access("isAnonymous() or hasAuthority('READ_MENU')")
                    .mvcMatchers(HttpMethod.POST, "/menu").hasAuthority("WRITE_MENU")
                    .mvcMatchers(HttpMethod.GET, "/order").hasAuthority("READ_ORDER")
                    .mvcMatchers(HttpMethod.POST, "/order").hasAuthority("WRITE_ORDER")
                    .anyRequest().authenticated().and()
                .formLogin() // 使用表单登录
                    .loginPage("/login").permitAll() // 设置登录页地址，全员可访问
                    .defaultSuccessUrl("/order")
                    .failureUrl("/login")
                    .loginProcessingUrl("/doLogin")
                    .usernameParameter("user")
                    .passwordParameter("pwd").and()
                .httpBasic().and() // 使用HTTP Basic认证
                .rememberMe()
                    .key("binarytea")
                    .rememberMeParameter("remember")
                    .tokenValiditySeconds(24 * 60 * 60)
                    .tokenRepository(persistentTokenRepository) // 配置持久化令牌
                    .userDetailsService(userDetailsService).and()
                .logout()
                    .logoutSuccessUrl("/")
                    .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", "GET"),
                        new AntPathRequestMatcher("/logout", "POST")));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/error", "/static/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtPreAuthenticatedAuthenticationProvider)
                .userDetailsService(userDetailsService);
    }
}
