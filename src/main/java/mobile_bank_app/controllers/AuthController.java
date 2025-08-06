package mobile_bank_app.controllers;

import mobile_bank_app.DTOs.AuthRequest;
import mobile_bank_app.jwt.JwtUtil;
import mobile_bank_app.repositories.UserRepository;
import mobile_bank_app.models.UserModel;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        UserModel user = new UserModel();
        if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required!");
        }
        if (!userRepository.findByUsername(authRequest.getUsername()).isEmpty()) {
            System.out.println("Found user with username: " + userRepository.findByUsername(authRequest.getUsername()).get());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists!");
        }

        user.setUsername(authRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        Optional<UserModel> userOpt = userRepository.findByUsername(authRequest.getUsername());
        System.out.println("Username from request: " + authRequest.getUsername() + "; Pssword from response: " + authRequest.getPassword());
        if (userOpt.isPresent()) {System.out.println("found user:" + userOpt.get());}
        if (userOpt.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(), userOpt.get().getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate token
        String token = jwtUtil.generateToken(authRequest.getUsername());
        System.out.println("JWT token: " + token + "validation status: " + jwtUtil.validateToken(token));
        // Store token in cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);

        return ResponseEntity.ok("Login successful");
    }
}
