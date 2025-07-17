package mobile_bank_app.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import mobile_bank_app.jwt.JwtUtil;
import mobile_bank_app.models.TransactionsModel;
import mobile_bank_app.models.UserModel;
import mobile_bank_app.repositories.TransactionsRepository;
import mobile_bank_app.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class TransactionsController {

    private final JwtUtil jwtUtil = new JwtUtil();
    private final UserRepository userRepository;
    private final TransactionsRepository transactionsRepository;

    public TransactionsController(final UserRepository userRepository, final TransactionsRepository transactionsRepository) {
        this.userRepository = userRepository;
        this.transactionsRepository = transactionsRepository;
    }
    @GetMapping("/get_transactions_by_user_id")
    public List<TransactionsModel> getAccountsByUsername(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("jwt"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token == null || !jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String username = jwtUtil.extractUsername(token);
        Optional<UserModel> userOptional = userRepository.findByUsername(username);
        UserModel user = userOptional.orElse(null);
        System.out.println("user: " + user + "found by username:" + username);

        if (user != null) {
            System.out.println("found transactions: " + transactionsRepository.findAllByUserId(user.getId()));
            return transactionsRepository.findAllByUserId(user.getId());
        } else {
            throw new RuntimeException("No user found");
        }
    }



}
