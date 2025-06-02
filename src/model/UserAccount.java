package model;

public class UserAccount {
    private int accountId;
    private String username;
    private String password;
    private String borrowerId;
    private String borrowerName;  // For display purposes

    public UserAccount(int accountId, String username, String password, String borrowerId, String borrowerName) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
} 