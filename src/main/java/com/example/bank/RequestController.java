/*package com.example.bank;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RequestController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private JwtFilter jwtFilter;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private String token;

    @Autowired
    public RequestController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                             UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            // Store token in JwtFilter
            String sessionId = session.getId();
            jwtFilter.storeToken(sessionId, token);
            System.out.println("Login successful for user: " + loginRequest.getUsername() + ", Session ID: " + sessionId);
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid credentials\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"An unexpected error occurred during login.\"}");
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\": \"Username '" + user.getUsername() + "' is already taken.\"}");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        usersRepository.save(user);
        return ResponseEntity.ok("{\"message\": \"User registered successfully\"}");
    }

    @GetMapping("/index.html")
    public ModelAndView serveIndexPage(HttpServletRequest request) {
        Boolean tokenValid = (Boolean) request.getAttribute("tokenValid");
        String errorMessage = (String) request.getAttribute("errorMessage");

        if (tokenValid == null || !tokenValid) {
            System.out.println("Token invalid or not provided, redirecting to login: " + errorMessage);
            return new ModelAndView("redirect:/login.html");
        }

        System.out.println("Token valid, serving index page");
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("contentVisible", true); // Optional: Pass data to template
        return modelAndView;
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}*/