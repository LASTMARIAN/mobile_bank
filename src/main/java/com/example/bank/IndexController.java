package com.example.bank;

import com.example.bank.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
/*
@Controller
public class IndexController {
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                return "index";
            }
        }
        return "redirect:/login.html";
    }
}*/
