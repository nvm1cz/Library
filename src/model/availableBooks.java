package model;

public class availableBooks {
    private int bookId;
    private String title;
    private String authors;
    private String genres;
    private int totalCopies;
    private int availableCopies;
    private int totalBorrows;
    private double avgRating;

    public availableBooks(int bookId, String title, String authors, String genres, int totalCopies, int availableCopies, int totalBorrows, double avgRating) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.totalBorrows = totalBorrows;
        this.avgRating = avgRating;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getGenres() {
        return genres;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTotalBorrows(int totalBorrows) {
        this.totalBorrows = totalBorrows;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
} 