package mobile_bank_app;

public class DTO {
    static public class AuthRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static public class TransferRequest {
        private Long fromAccountId;
        private String toAccountId;
        private long amount;

        public Long getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
        public String getToAccountId() { return toAccountId; }
        public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }
        public long getAmount() { return amount; }
        public void setAmount(long amount) { this.amount = amount; }
    }
}
