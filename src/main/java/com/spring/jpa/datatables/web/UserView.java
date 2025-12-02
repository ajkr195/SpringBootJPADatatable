package com.spring.jpa.datatables.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserView {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
