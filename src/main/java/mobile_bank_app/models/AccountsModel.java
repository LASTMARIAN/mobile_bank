package mobile_bank_app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class AccountsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "user_id")
    private long userId;

    private long balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount_id() {
        return accountId;
    }

    public void setAccount_id(String account_id) {
        this.accountId = account_id;
    }

    public long getUser_id() {
        return userId;
    }

    public void setUser_id(long user_id) {
        this.userId = user_id;
    }

    public long getBalance() { return balance; }

    public void setBalance(long balance) { this.balance = balance; }
}
