package com.openclassrooms.springsecurity.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LoginController.class})
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    void userAuthenticated() throws Exception {
        mockMvc.perform(formLogin("/login").user("springuser").password("springuser"))
                .andExpect(authenticated())
                .andDo(print());
    }

    @Test
    void displayLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please sign in")))
                .andDo(print());
    }

    // Fixes the non-sens of the previous commit!!!!!!
    @Test
    void greetMockGitHubUser() throws Exception {

        mockMvc.perform(get("/gituser").with(user("mockuser")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome Github user")))
                .andDo(print());
    }

    // New Test
    @Test
    void greetSpringUser() throws Exception {

        mockMvc.perform(get("/user").with(user("mockuser")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome User")))
                .andDo(print());
    }

    // Fixes the non-sens of the previous commit!!!!!!
    @Test
    void greetAdminUser() throws Exception {

        mockMvc.perform(get("/admin").with(user("mockadmin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome Admin")))
                .andDo(print());
    }

    @Test
    void gitLoginOK() throws Exception {
        mockMvc.perform(get("/gituser").with(oidcLogin()))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome Github user!"))
                .andDo(print());
    }


}