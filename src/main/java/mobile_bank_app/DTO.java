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
        private String fromAccountId;
        private String toAccountId;
        private int amount;

        public String getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(String fromAccountId) { this.fromAccountId = fromAccountId; }
        public String getToAccountId() { return toAccountId; }
        public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
    }
}




