package com.franklin.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.franklin.backend.annotation.GetUser;
import com.franklin.backend.entity.User;

@Controller
public class FrontendController {

    @GetMapping(path = "/")
    public String index() {
        return "index";
    }

    @GetMapping(path = "/login")
    public String login(@GetUser User user) {
        if (user == null) {
            return "/login";
        }
        return "redirect:/";
    }

    @GetMapping(path = "/new_user")
    public String newUser() {
        return "newUser";
    }
}