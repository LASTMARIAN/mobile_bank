package mobile_bank_app.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class AccountsModel {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "user_id")
    private long userId;

    private BigDecimal balance;

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

    public BigDecimal getBalance() { return balance; }

    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
