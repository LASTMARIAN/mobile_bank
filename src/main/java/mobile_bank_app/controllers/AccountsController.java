package mobile_bank_app.controllers;

import mobile_bank_app.DTO.TransferRequest;
import mobile_bank_app.models.AccountsModel;
import mobile_bank_app.models.UserModel;
import mobile_bank_app.repositories.AccountRepository;
import mobile_bank_app.repositories.UserRepository;
import mobile_bank_app.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class AccountsController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AccountsController(AccountRepository accountRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/get_accounts_by_user_id")
    public List<AccountsModel> getAccountsByUsername(HttpServletRequest request) {
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
        if (user!=null) {
            System.out.println("Find users by this id: " + user.getId());
            System.out.println("Users found:" + accountRepository.findAllByUserId(user.getId()));
            return accountRepository.findAllByUserId(user.getId());
        }
        else {
            throw new RuntimeException("No user found");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        System.out.println("Method executed");
        AccountsModel sender_account = accountRepository.findById(transferRequest.getFromAccountId()).orElse(null);
        AccountsModel receiver_account = accountRepository.findByAccountId(transferRequest.getToAccountId()).orElse(null);
        System.out.println("Sender account: " + sender_account + "receiver account: " + receiver_account);
        if (sender_account != null && receiver_account != null) {
            if (sender_account.getBalance() >= transferRequest.getAmount()) {
                sender_account.setBalance(sender_account.getBalance() - transferRequest.getAmount());
                receiver_account.setBalance(receiver_account.getBalance() + transferRequest.getAmount());
                accountRepository.save(sender_account);
                accountRepository.save(receiver_account);
                return ResponseEntity.ok("Transfer successful");
            }
            else {
                return ResponseEntity.badRequest().body("Insufficient funds");
            }
        }
        else {
            return ResponseEntity.badRequest().body("Receiving account not found");
        }
    }


}
