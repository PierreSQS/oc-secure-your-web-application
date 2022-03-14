package com.openclassrooms.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class LoginController {

    // Fixes the non-sens of the previous code!!!!!!
    @RolesAllowed("USER")
    @GetMapping("user")
    public String greatUser() {
        return "Welcome User!";
    }

    @RolesAllowed({"USER","ADMIN"})
    @GetMapping("admin")
    public String greatAdmin() {
        return "Welcome Admin";
    }


    // Fixes the non-sens of the previous code!!!!!!
    @GetMapping("gituser")
    public String getGithub()
    {
        return "Welcome Github user!";
    }
}
