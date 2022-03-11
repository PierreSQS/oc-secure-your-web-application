package com.openclassrooms.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

// TODO the mappings of the controller should be checked !!
@RestController
public class LoginController {

    @RolesAllowed("USER")
    @GetMapping("**")
    public String greatUser() {
        return "Welcome User!";
    }

    @RolesAllowed({"USER","ADMIN"})
    @GetMapping("admin")
    public String greatAdmin() {
        return "Welcome Admin";
    }


    @GetMapping("*")
    public String getGithub()
    {
        return "Welcome Github user!";
    }
}
