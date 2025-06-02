package model;

import java.time.LocalDate;

public class BorrowEntry {
    private int entryId;
    private String borrowerId;
    private String borrowerName;
    private int bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private int processedBy;
    private String status;

    public BorrowEntry(int entryId, String borrowerId, String borrowerName, int bookId, 
                      String bookTitle, LocalDate borrowDate, LocalDate returnDate, 
                      int processedBy) {
        this.entryId = entryId;
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.processedBy = processedBy;
        this.status = returnDate == null ? "Borrowed" : "Returned";
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

    public int getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public int getProcessedBy() {
        return processedBy;
    }

    public String getStatus() {
        return status;
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

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.status = returnDate == null ? "Borrowed" : "Returned";
    }

    public void setProcessedBy(int processedBy) {
        this.processedBy = processedBy;
    }
} 