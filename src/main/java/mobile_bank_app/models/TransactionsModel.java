package mobile_bank_app.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name="transactions")
public class TransactionsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(name="user_id")
    private Long userId;

    private Timestamp time;

    @Column(name="to_account_id")
    private String to_accountId;

    @Column(name="from_account_id")
    private String from_accountId;

    private BigDecimal amount;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getTo_accountId() {
        return to_accountId;
    }

    public void setTo_accountId(String to_accountId) {
        this.to_accountId = to_accountId;
    }

    public String getFrom_accountId() {
        return from_accountId;
    }

    public void setFrom_accountId(String from_accountId) {
        this.from_accountId = from_accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
