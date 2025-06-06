package model;

import java.time.LocalDateTime;

public class CurrentBorrow {
    private int entryId;
    private String borrowerId;
    private String borrowerName;
    private String bookTitle;
    private LocalDateTime borrowDate;

    public CurrentBorrow(int entryId, String borrowerId, String borrowerName, 
                        String bookTitle, LocalDateTime borrowDate) {
        this.entryId = entryId;
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
    }

    public int getEntryId() {
        return entryId;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }
} 