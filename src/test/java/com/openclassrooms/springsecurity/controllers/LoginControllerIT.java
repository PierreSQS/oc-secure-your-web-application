package com.openclassrooms.springsecurity.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
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

    @WithMockUser
    @Test
    void userWithMockUser() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome User!"))
                .andDo(print());
    }

    @Test
    void gitLoginOK() throws Exception {
        mockMvc.perform(get("/gituser").with(oidcLogin()))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome Github user!"))
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

    @Test
    void adminAccessPathOK() throws Exception {
        mockMvc.perform(get("/admin").with(user("mockadmin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void adminAccessPathNOK() throws Exception {
        mockMvc.perform(get("/admin").with(user("mockadmin").roles("USER")))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}