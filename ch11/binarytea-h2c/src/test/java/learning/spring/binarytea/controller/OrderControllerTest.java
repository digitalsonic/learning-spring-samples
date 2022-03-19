package learning.spring.binarytea.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class OrderControllerTest {
    private MockMvc mockMvc;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        jdbcTemplate = new JdbcTemplate(wac.getBean(DataSource.class));
    }

    @AfterEach
    void tearDown() {
        mockMvc = null;
        jdbcTemplate = null;
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(formLogin("/doLogin")
                .user("user", "LiLei")
                .password("pwd", "binarytea"))
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/order"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(logout())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testOrderPageWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/order")
//                        .header("Accept", "text/html") // 模拟浏览器
        )
                .andExpect(unauthenticated())
//                .andExpect(status().is3xxRedirection()) // 浏览器里是跳转，否则是401走HTTP Basic
//                .andExpect(redirectedUrlPattern("**/login"))
        ;
    }

    @Test
    void testOrderPageWithAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/order").with(user("LiLei")
                .authorities(AuthorityUtils.createAuthorityList("READ_ORDER", "ROLE_TEA_MAKER"))))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/order").with(httpBasic("LiLei", "binarytea")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testOrderPageWithPersistentToken() throws Exception {
        mockMvc.perform(post("/doLogin")
                .param("user", "LiLei")
                .param("pwd", "binarytea")
                .param("remember", "1")
                .with(csrf())) // 提交的内容里要包含一个CSRF令牌
                .andExpect(authenticated())
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().maxAge("remember-me", 24 * 60 * 60));
        assertEquals(1,
                jdbcTemplate.queryForObject("select count(1) from persistent_logins", Integer.class));
        assertEquals("LiLei",
                jdbcTemplate.queryForObject("select username from persistent_logins", String.class));
    }

//    @Test
    void testModifyOrdersToPaidWithCsrfFail() throws Exception {
        mockMvc.perform(put("/order")
                .param("id", "1").with(userLiLei()))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(put("/order")
                .param("id", "1").with(userLiLei())
                .with(csrf().useInvalidToken()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testModifyOrdersToPaid() throws Exception {
        mockMvc.perform(put("/order").param("id", "1")
                .with(userLiLei()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("success_count", 1));
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userLiLei() {
        return user("LiLei")
                .authorities(AuthorityUtils.createAuthorityList("READ_ORDER", "ROLE_TEA_MAKER"));
    }
}