package learning.spring.binarytea;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import learning.spring.binarytea.support.RoleBasedJdbcUserDetailsManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;

import javax.sql.DataSource;

@SpringBootApplication
@EnableCaching
public class BinaryTeaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinaryTeaApplication.class, args);
    }

    @Bean
    public JodaMoneyModule jodaMoneyModule() {
        return new JodaMoneyModule();
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean("/login")
    public UrlFilenameViewController loginController() {
        UrlFilenameViewController controller = new UrlFilenameViewController();
        controller.setSupportedMethods(HttpMethod.GET.name());
        controller.setSuffix(".html");
        return controller;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider jwtPreAuthenticatedAuthenticationProvider(ObjectProvider<DataSource> dataSources,
                                                                                            UserDetailsService userDetailsService) {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(
                new UserDetailsByNameServiceWrapper<>(userDetailsService));
        return provider;
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
}
