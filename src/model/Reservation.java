package model;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private String username;
    private String bookTitle;
    private LocalDateTime reservationDate;
    private String status;

    public Reservation(int id, String username, String bookTitle, LocalDateTime reservationDate, String status) {
        this.id = id;
        this.username = username;
        this.bookTitle = bookTitle;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 