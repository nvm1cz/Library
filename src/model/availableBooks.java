package model;

public class availableBooks {
    private String title;
    private String author;
    private String genre;
    private String date;
    private int quantity;

    public availableBooks(String title, String author, String genre, String date, int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
} 