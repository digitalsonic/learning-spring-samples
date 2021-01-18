package learning.spring.binarytea.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class OrderControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        mockMvc = null;
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
        mockMvc.perform(get("/order").with(user("LiLei")))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/order").with(httpBasic("LiLei", "binarytea")))
                .andExpect(status().is2xxSuccessful());
    }
}