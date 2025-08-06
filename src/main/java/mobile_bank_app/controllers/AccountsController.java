package mobile_bank_app.controllers;

import mobile_bank_app.DTOs.TransferRequest;
import mobile_bank_app.models.AccountsModel;
import mobile_bank_app.models.UserModel;
import mobile_bank_app.models.TransactionsModel;
import mobile_bank_app.repositories.AccountRepository;
import mobile_bank_app.repositories.UserRepository;
import mobile_bank_app.repositories.TransactionsRepository;
import mobile_bank_app.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class AccountsController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionsRepository transactionsRepository;
    private final JwtUtil jwtUtil = new JwtUtil();

    public AccountsController(final AccountRepository accountRepository, final UserRepository userRepository, final TransactionsRepository transactionsRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionsRepository = transactionsRepository;
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
    private TransactionsModel createTransaction(String type, Long userId, String from, String to, BigDecimal amount) {
        TransactionsModel t = new TransactionsModel();
        t.setType(type);
        t.setUserId(userId);
        t.setFrom_accountId(from);
        t.setTo_accountId(to);
        if (type.equals("outcoming")) {
            t.setAmount(amount.negate());
        }
        else {
            t.setAmount(amount);
        }
        return t;
    }

    @PostMapping("/transfer")
    private ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        System.out.println("Method executed");
        AccountsModel sender_account = accountRepository.findByAccountId(transferRequest.getFromAccountId()).orElse(null);
        AccountsModel receiver_account = accountRepository.findByAccountId(transferRequest.getToAccountId()).orElse(null);
        System.out.println("Sender account: " + sender_account + "receiver account: " + receiver_account);

        if (sender_account != null && receiver_account != null) {
            if (sender_account.getBalance().compareTo(transferRequest.getAmount()) >= 0) {
                if (sender_account.getUser_id() == receiver_account.getUser_id()) {

                    sender_account.setBalance(sender_account.getBalance().subtract(transferRequest.getAmount()));
                    receiver_account.setBalance(receiver_account.getBalance().add(transferRequest.getAmount()));
                    TransactionsModel transaction = createTransaction("internal", sender_account.getUser_id(),
                            sender_account.getAccount_id(), receiver_account.getAccount_id(), transferRequest.getAmount());
                    transactionsRepository.save(transaction);
                    accountRepository.save(sender_account);
                    accountRepository.save(receiver_account);
                    return ResponseEntity.ok("Transfer successful");
                }
                else {
                    sender_account.setBalance(sender_account.getBalance().subtract(transferRequest.getAmount()));
                    TransactionsModel sender_transaction = createTransaction("outcoming", sender_account.getUser_id(),
                            sender_account.getAccount_id(), receiver_account.getAccount_id(), transferRequest.getAmount());
                    transactionsRepository.save(sender_transaction);
                    receiver_account.setBalance(receiver_account.getBalance().add(transferRequest.getAmount()) );
                    TransactionsModel receiver_transaction = createTransaction("incoming", receiver_account.getUser_id(),
                            sender_account.getAccount_id(), receiver_account.getAccount_id(), transferRequest.getAmount());
                    transactionsRepository.save(receiver_transaction);
                    accountRepository.save(sender_account);
                    accountRepository.save(receiver_account);
                    return ResponseEntity.ok("Transfer successful");
                }
            }
            else {
                return ResponseEntity.badRequest().body("Insufficient funds");
            }
        }
        else {
            return ResponseEntity.badRequest().body("Receiving account not found");
        }
    }

    @DeleteMapping("/delete_account")
    public ResponseEntity<String> deleteAccount(HttpServletRequest request, @RequestBody Map<String, String> payload) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("jwt"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token == null || !jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String accountId = payload.get("accountId");
        Optional<AccountsModel> accountOptional = accountRepository.findByAccountId(accountId);
        if (accountOptional.isPresent()) {
            AccountsModel account = accountOptional.get();
            accountRepository.delete(account);
            return ResponseEntity.ok("Account deleted successfully");
        }
        else {
            return ResponseEntity.badRequest().body("Account not found");
        }

    }

    @PostMapping("/create_account")
    public ResponseEntity<String> createAccount(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("jwt"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token == null || !jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }
        AccountsModel last_account = accountRepository.findLastAccount();
        System.out.println("Last account: " + last_account.getAccount_id());
        long lastAccountId = Long.parseLong(last_account.getAccount_id().substring(4));
        AccountsModel new_account = new AccountsModel();
        String new_account_id = Long.toString(lastAccountId + 1);
        System.out.println("New account id: " + new_account_id);
        if (!accountRepository.findByAccountId("ACC_" + new_account_id).isPresent()) {
            new_account.setAccount_id("ACC_" + (lastAccountId + 1));
            String username = jwtUtil.extractUsername(token);
            UserModel current_user = userRepository.findByUsername(username).orElse(null);
            new_account.setUser_id(current_user.getId());
            new_account.setBalance(BigDecimal.valueOf(100.00));
            accountRepository.save(new_account);
            return ResponseEntity.ok("Account is succesfully created");
        }
        else {
            return ResponseEntity.badRequest().body("Account is present");
        }
    }



}
