package learning.spring.binarytea;

import learning.spring.binarytea.support.RoleBasedJdbcUserDetailsManager;
import learning.spring.binarytea.support.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.ObjectProvider;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private ObjectProvider<DataSource> dataSources;

    @Bean("/login")
    public UrlFilenameViewController loginController() {
        UrlFilenameViewController controller = new UrlFilenameViewController();
        controller.setSupportedMethods(HttpMethod.GET.name());
        controller.setSuffix(".html");
        return controller;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider jwtPreAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(
                new UserDetailsByNameServiceWrapper<>(userDetailsService(dataSources)));
        return provider;
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
                    .tokenRepository(persistentTokenRepository(dataSources)) // 配置持久化令牌
                    .userDetailsService(userDetailsService(dataSources)).and()
                .logout()
                    .logoutSuccessUrl("/")
                    .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", "GET"),
                        new AntPathRequestMatcher("/logout", "POST")));
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(ObjectProvider<DataSource> dataSources) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSources.getIfAvailable());
        tokenRepository.setCreateTableOnStartup(false);
        return tokenRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(ObjectProvider<DataSource> dataSources) {
        RoleBasedJdbcUserDetailsManager userDetailsManager = new RoleBasedJdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSources.getIfAvailable());
        UserDetails manager = User.builder()
                .username("HanMeimei")
                .password("{bcrypt}$2a$10$iAty2GrJu9WfpksIen6qX.vczLmXlp.1q1OHBxWEX8BIldtwxHl3u")
                .roles("MANAGER")
                .build();
        userDetailsManager.createUser(manager);
        return userDetailsManager;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/error", "/static/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtPreAuthenticatedAuthenticationProvider())
                .userDetailsService(userDetailsService(dataSources));
    }
}
