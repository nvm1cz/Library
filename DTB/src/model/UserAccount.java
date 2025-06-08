package model;

public class UserAccount {
    private String username;
    private String password;
    private String borrowerId;
    private String borrowerName;  // For display purposes

    public UserAccount(String borrowerId, String username, String password) {
        this.username = username;
        this.password = password;
        this.borrowerId = borrowerId;
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