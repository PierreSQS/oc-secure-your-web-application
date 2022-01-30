package com.openclassrooms.springsecurity.controllers;

import com.openclassrooms.springsecurity.configuration.SpringSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {SpringSecurityConfig.class})
class LoginControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andDo(print());
    }

    @Test
    void userLoginOK() throws Exception {
        mockMvc.perform(formLogin("/login").user("springuser").password("springuser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @Test
    void adminLoginOK() throws Exception {
        mockMvc.perform(formLogin("/login").user("springadmin").password("springadmin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @Test
    void adminLoginWrongCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("springadmin").password("wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION",isA(BadCredentialsException.class)))
                .andExpect(redirectedUrl("/login?error"))
                .andDo(print());
    }

    @Test
    void adminLoginSuccessful() throws Exception {
        mockMvc.perform(formLogin("/login").user("springadmin").password("springadmin"))
                .andExpect(authenticated())
                .andDo(print());
    }

    @Test
    void adminLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("springadmin").password("wrong-password"))
                .andExpect(unauthenticated())
                .andDo(print());
    }
}