package com.openclassrooms.springsecurity.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
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

    // TODO seems to work but have to be analyzed deeper!!! Check the controller therefore.
    // Why ist is it going to LoginController#getGithub() Handler????
    @Test
    void greetMockGitHubUser() throws Exception {

        mockMvc.perform(get("/**").with(user("MockUser")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome Github user")))
                .andDo(print());
    }
}