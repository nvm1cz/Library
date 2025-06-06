package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Optional;
import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.sql.CallableStatement;

import dao.DBConnect;
import model.getData;
import model.BorrowEntry;
import model.availableBooks;
import model.UserAccount;
import model.Reservation;
import model.CurrentBorrow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.scene.control.TableCell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AdminDashboardController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button manageBooks_btn;

    @FXML
    private Button manageUsers_btn;

    @FXML
    private Button borrowRecords_btn;

    @FXML
    private Button reservations_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane mainCenter_form;

    @FXML
    private AnchorPane manageBooks_form;

    @FXML
    private TableView<availableBooks> books_tableView;

    @FXML
    private TableColumn<availableBooks, String> col_bookTitle;

    @FXML
    private TableColumn<availableBooks, String> col_author;

    @FXML
    private TableColumn<availableBooks, Integer> col_totalCopies;

    @FXML
    private TableColumn<availableBooks, Integer> col_availableCopies;

    @FXML
    private TableColumn<availableBooks, Integer> col_totalBorrows;

    @FXML
    private TableColumn<availableBooks, String> col_genre;

    @FXML
    private AnchorPane manageUsers_form;

    @FXML
    private TableView<UserAccount> users_tableView;

    @FXML
    private TableColumn<UserAccount, Integer> col_accountId;

    @FXML
    private TableColumn<UserAccount, String> col_username;

    @FXML
    private TableColumn<UserAccount, String> col_password;

    @FXML
    private TableColumn<UserAccount, String> col_borrowerId;

    @FXML
    private TableColumn<UserAccount, String> col_borrowerName;

    @FXML
    private TextField username_field;

    @FXML
    private TextField password_field;

    @FXML
    private ComboBox<String> userType_combo;

    @FXML
    private TextField borrower_name;

    @FXML
    private Button addUser_btn;

    @FXML
    private Button updateUser_btn;

    @FXML
    private Button deleteUser_btn;

    @FXML
    private Button clearUser_btn;

    @FXML
    private TextField book_title;

    @FXML
    private TextField book_author;

    @FXML
    private TextField book_totalCopies;

    @FXML
    private TextField book_quantity;

    @FXML
    private Button addBook_btn;

    @FXML
    private Button deleteBook_btn;

    @FXML
    private Button clearBook_btn;

    @FXML
    private TextField delete_quantity;

    @FXML
    private AnchorPane borrowRecords_form;

    @FXML
    private TableView<BorrowEntry> borrow_tableView;

    @FXML
    private TableColumn<BorrowEntry, String> col_borrowId;

    @FXML
    private TableColumn<BorrowEntry, String> col_borrowName;

    @FXML
    private TableColumn<BorrowEntry, String> col_borrowBook;

    @FXML
    private TableColumn<BorrowEntry, LocalDateTime> col_borrowDate;

    @FXML
    private TableColumn<BorrowEntry, LocalDateTime> col_returnDate;

    @FXML
    private TableColumn<BorrowEntry, String> col_borrowStatus;

    @FXML
    private ComboBox<Borrower> borrower_combo;

    @FXML
    private ComboBox<availableBooks> book_combo;

    @FXML
    private Button addBorrow_btn;

    @FXML
    private Button returnBook_btn;

    @FXML
    private Button clearBorrow_btn;

    @FXML
    private AnchorPane reservations_form;

    @FXML
    private TableView<Reservation> reservations_tableView;

    @FXML
    private TableColumn<Reservation, Integer> col_reservationId;

    @FXML
    private TableColumn<Reservation, String> col_reservationUser;

    @FXML
    private TableColumn<Reservation, String> col_reservationBook;

    @FXML
    private TableColumn<Reservation, LocalDateTime> col_reservationDate;

    @FXML
    private TableColumn<Reservation, String> col_reservationStatus;

    @FXML
    private Button currentBorrows_btn;

    @FXML
    private AnchorPane currentBorrows_form;

    @FXML
    private Label currentForm_label;

    @FXML
    private TableView<CurrentBorrow> currentBorrows_tableView;

    @FXML
    private TableColumn<CurrentBorrow, Integer> col_currentBorrowId;

    @FXML
    private TableColumn<CurrentBorrow, String> col_currentBorrowerId;

    @FXML
    private TableColumn<CurrentBorrow, String> col_currentBorrowerName;

    @FXML
    private TableColumn<CurrentBorrow, String> col_currentBookTitle;

    @FXML
    private TableColumn<CurrentBorrow, LocalDateTime> col_currentBorrowDate;

    @FXML
    private ComboBox<String> searchTypeCombo;

    @FXML
    private TextField search_field;

    @FXML
    private TextField book_genre;

    @FXML
    private TableColumn<availableBooks, Double> col_avgRating;

    @FXML
    private Button fulfilled_btn;
    @FXML
    private Button cancel_btn;

    @FXML
    private TextField user_search_field;

    @FXML
    private TextField borrowerName_search_field;
    @FXML
    private TextField bookTitle_search_field;
    @FXML
    private DatePicker recordDate_from;
    @FXML
    private DatePicker recordDate_to;

    private String selectedReservationStatus = null;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    private static class Borrower {
        private String id;
        private String name;

        public Borrower(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return name;
        }
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == manageBooks_btn) {
            manageBooks_form.setVisible(true);
            manageUsers_form.setVisible(false);
            borrowRecords_form.setVisible(false);
            reservations_form.setVisible(false);
            currentBorrows_form.setVisible(false);
            
            currentForm_label.setText("Manage Books");
            showBooks();
        } else if (event.getSource() == manageUsers_btn) {
            manageBooks_form.setVisible(false);
            manageUsers_form.setVisible(true);
            borrowRecords_form.setVisible(false);
            reservations_form.setVisible(false);
            currentBorrows_form.setVisible(false);
            
            currentForm_label.setText("Manage Users");
            showUsers();
        } else if (event.getSource() == borrowRecords_btn) {
            manageBooks_form.setVisible(false);
            manageUsers_form.setVisible(false);
            borrowRecords_form.setVisible(true);
            reservations_form.setVisible(false);
            currentBorrows_form.setVisible(false);
            
            currentForm_label.setText("Borrow Records");
            showBorrowRecords();
        } else if (event.getSource() == reservations_btn) {
            manageBooks_form.setVisible(false);
            manageUsers_form.setVisible(false);
            borrowRecords_form.setVisible(false);
            reservations_form.setVisible(true);
            currentBorrows_form.setVisible(false);
            
            currentForm_label.setText("Reservations");
            showReservations();
        } else if (event.getSource() == currentBorrows_btn) {
            manageBooks_form.setVisible(false);
            manageUsers_form.setVisible(false);
            borrowRecords_form.setVisible(false);
            reservations_form.setVisible(false);
            currentBorrows_form.setVisible(true);
            
            currentForm_label.setText("Current Borrows");
            showCurrentBorrows();
        }
    }

    public ObservableList<availableBooks> booksList(String searchTerm, String searchType) {
        ObservableList<availableBooks> bookList = FXCollections.observableArrayList();
        String sql = "SELECT b.BookID, b.Title, GROUP_CONCAT(DISTINCT a.FullName SEPARATOR ', ') as Authors, " +
                    "GROUP_CONCAT(DISTINCT g.Name SEPARATOR ', ') as Genres, " +
                    "b.TotalCopies, b.AvailableCopies, b.TotalBorrows, " +
                    "AVG(r.Rating) as AvgRating " +
                    "FROM Book b " +
                    "LEFT JOIN BookAuthor ba ON b.BookID = ba.BookID " +
                    "LEFT JOIN Author a ON ba.AuthorID = a.AuthorID " +
                    "LEFT JOIN BookGenre bg ON b.BookID = bg.BookID " +
                    "LEFT JOIN Genre g ON bg.GenreID = g.GenreID " +
                    "LEFT JOIN BorrowEntry be ON b.BookID = be.BookID " +
                    "LEFT JOIN Review r ON be.EntryID = r.EntryID " +
                    "WHERE 1=1";
        if (searchTerm != null && !searchTerm.trim().isEmpty() && searchType != null) {
            switch (searchType) {
                case "Title":
                    sql += " AND LOWER(b.Title) LIKE LOWER(?)";
                    break;
                case "Author":
                    sql += " AND LOWER(a.FullName) LIKE LOWER(?)";
                    break;
                case "Genre":
                    sql += " AND LOWER(g.Name) LIKE LOWER(?)";
                    break;
                default:
                    sql += " AND (LOWER(b.Title) LIKE LOWER(?) OR LOWER(a.FullName) LIKE LOWER(?) OR LOWER(g.Name) LIKE LOWER(?))";
            }
        }
        sql += " GROUP BY b.BookID";
        connect = DBConnect.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            if (searchTerm != null && !searchTerm.trim().isEmpty() && searchType != null) {
                String searchPattern = "%" + searchTerm.trim() + "%";
                switch (searchType) {
                    case "Title":
                    case "Author":
                    case "Genre":
                        prepare.setString(1, searchPattern);
                        break;
                    default:
                        prepare.setString(1, searchPattern);
                        prepare.setString(2, searchPattern);
                        prepare.setString(3, searchPattern);
                }
            }
            result = prepare.executeQuery();
            while (result.next()) {
                availableBooks book = new availableBooks(
                        result.getInt("BookID"),
                        result.getString("Title"),
                        result.getString("Authors"),
                        result.getString("Genres"),
                        result.getInt("TotalCopies"),
                        result.getInt("AvailableCopies"),
                        result.getInt("TotalBorrows"),
                        result.getDouble("AvgRating")
                );
                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public void showBooks() {
        String searchTerm = search_field != null ? search_field.getText() : null;
        String searchType = searchTypeCombo != null ? searchTypeCombo.getValue() : null;
        ObservableList<availableBooks> listBooks = booksList(searchTerm, searchType);
        col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        col_genre.setCellValueFactory(new PropertyValueFactory<>("genres"));
        col_totalCopies.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        col_availableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        col_totalBorrows.setCellValueFactory(new PropertyValueFactory<>("totalBorrows"));
        col_avgRating.setCellValueFactory(new PropertyValueFactory<>("avgRating"));
        books_tableView.setItems(listBooks);
    }

    @FXML
    public void searchBooks() {
        showBooks();
    }

    @FXML
    public void clearSearch() {
        if (search_field != null) search_field.clear();
        showBooks();
    }

    public ObservableList<BorrowEntry> borrowList(String borrowerName, String bookTitle, LocalDate recordFrom, LocalDate recordTo) {
        ObservableList<BorrowEntry> list = FXCollections.observableArrayList();
        String sql = "SELECT be.*, b.FullName as BorrowerName, bk.Title as BookTitle " +
                    "FROM BorrowEntry be " +
                    "JOIN Borrower b ON be.BorrowerID = b.BorrowerID " +
                    "JOIN Book bk ON be.BookID = bk.BookID WHERE 1=1 ";
        if (borrowerName != null && !borrowerName.trim().isEmpty()) {
            sql += "AND LOWER(b.FullName) LIKE ? ";
        }
        if (bookTitle != null && !bookTitle.trim().isEmpty()) {
            sql += "AND LOWER(bk.Title) LIKE ? ";
        }
        if (recordFrom != null) {
            sql += "AND (be.BorrowDate >= ? OR (be.ReturnDate IS NOT NULL AND be.ReturnDate >= ?)) ";
        }
        if (recordTo != null) {
            sql += "AND (be.BorrowDate <= ? OR (be.ReturnDate IS NOT NULL AND be.ReturnDate <= ?)) ";
        }
        sql += "ORDER BY be.EntryID DESC";
        connect = DBConnect.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            int idx = 1;
            if (borrowerName != null && !borrowerName.trim().isEmpty()) {
                prepare.setString(idx++, "%" + borrowerName.trim().toLowerCase() + "%");
            }
            if (bookTitle != null && !bookTitle.trim().isEmpty()) {
                prepare.setString(idx++, "%" + bookTitle.trim().toLowerCase() + "%");
            }
            if (recordFrom != null) {
                prepare.setDate(idx++, Date.valueOf(recordFrom));
                prepare.setDate(idx++, Date.valueOf(recordFrom));
            }
            if (recordTo != null) {
                prepare.setDate(idx++, Date.valueOf(recordTo));
                prepare.setDate(idx++, Date.valueOf(recordTo));
            }
            result = prepare.executeQuery();
            while (result.next()) {
                java.sql.Timestamp returnTimestamp = result.getTimestamp("ReturnDate");
                list.add(new BorrowEntry(
                    result.getInt("EntryID"),
                    result.getString("BorrowerID"),
                    result.getString("BorrowerName"),
                    result.getInt("BookID"),
                    result.getString("BookTitle"),
                    result.getTimestamp("BorrowDate").toLocalDateTime(),
                    returnTimestamp != null ? returnTimestamp.toLocalDateTime() : null,
                    result.getInt("ProcessedBy")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void showBorrowRecords() {
        String borrowerName = borrowerName_search_field.getText();
        String bookTitle = bookTitle_search_field.getText();
        LocalDate recordFrom = recordDate_from.getValue();
        LocalDate recordTo = recordDate_to.getValue();
        ObservableList<BorrowEntry> list = borrowList(borrowerName, bookTitle, recordFrom, recordTo);
        col_borrowId.setCellValueFactory(new PropertyValueFactory<>("entryId"));
        col_borrowName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        col_borrowBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        col_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        col_borrowStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        borrow_tableView.setItems(list);
    }

    @FXML
    public void searchBorrowRecords() {
        String borrowerName = borrowerName_search_field.getText();
        String bookTitle = bookTitle_search_field.getText();
        LocalDate recordFrom = recordDate_from.getValue();
        LocalDate recordTo = recordDate_to.getValue();
        ObservableList<BorrowEntry> list = borrowList(borrowerName, bookTitle, recordFrom, recordTo);
        col_borrowId.setCellValueFactory(new PropertyValueFactory<>("entryId"));
        col_borrowName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        col_borrowBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        col_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        col_borrowStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        borrow_tableView.setItems(list);
    }

    @FXML
    public void clearBorrowRecordSearch() {
        borrowerName_search_field.clear();
        bookTitle_search_field.clear();
        recordDate_from.setValue(null);
        recordDate_to.setValue(null);
        showBorrowRecords();
    }

    private void loadBorrowers() {
        String sql = "SELECT BorrowerID, FullName FROM Borrower ORDER BY FullName";
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            ObservableList<Borrower> borrowers = FXCollections.observableArrayList();
            while (result.next()) {
                borrowers.add(new Borrower(
                    result.getString("BorrowerID"),
                    result.getString("FullName")
                ));
            }
            
            // Set items for both ComboBoxes
            if (borrower_combo != null) {
                borrower_combo.setItems(borrowers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAvailableBooks() {
        String sql = "SELECT b.BookID, b.Title, GROUP_CONCAT(DISTINCT a.FullName SEPARATOR ', ') as Authors, " +
                    "GROUP_CONCAT(DISTINCT g.Name SEPARATOR ', ') as Genres, " +
                    "b.TotalCopies, b.AvailableCopies, b.TotalBorrows " +
                    "FROM Book b " +
                    "LEFT JOIN BookAuthor ba ON b.BookID = ba.BookID " +
                    "LEFT JOIN Author a ON ba.AuthorID = a.AuthorID " +
                    "LEFT JOIN BookGenre bg ON b.BookID = bg.BookID " +
                    "LEFT JOIN Genre g ON bg.GenreID = g.GenreID " +
                    "WHERE b.AvailableCopies > 0 " +
                    "GROUP BY b.BookID " +
                    "ORDER BY b.Title";
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            ObservableList<availableBooks> books = FXCollections.observableArrayList();
            while (result.next()) {
                books.add(new availableBooks(
                    result.getInt("BookID"),
                    result.getString("Title"),
                    result.getString("Authors"),
                    result.getString("Genres"),
                    result.getInt("TotalCopies"),
                    result.getInt("AvailableCopies"),
                    result.getInt("TotalBorrows"),
                    0.0 // avgRating mặc định cho combobox
                ));
            }
            book_combo.setItems(books);
            book_combo.setConverter(new StringConverter<availableBooks>() {
                @Override
                public String toString(availableBooks book) {
                    return book != null ? book.getTitle() : "";
                }

                @Override
                public availableBooks fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBorrow() {
        String sql = "INSERT INTO BorrowEntry (BorrowerID, BookID, BorrowDate, ProcessedBy) VALUES (?, ?, ?, ?)";
        
        connect = DBConnect.connectDB();
        
        try {
            Alert alert;
            
            if (borrower_combo.getValue() == null || book_combo.getValue() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select both borrower and book");
                alert.showAndWait();
                return;
            }

            // Add borrow entry - trigger will handle book count updates
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, borrower_combo.getValue().getId());
            prepare.setInt(2, book_combo.getValue().getBookId());
            prepare.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            prepare.setInt(4, getData.adminId);
            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added borrow record!");
            alert.showAndWait();

            showBorrowRecords();
            loadAvailableBooks();
            clearBorrowFields();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void returnBook() {
        CurrentBorrow selected = currentBorrows_tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a current borrow record to return");
            alert.showAndWait();
            return;
        }

        connect = DBConnect.connectDB();
        try {
            // Call the ReturnBook stored procedure
            String sql = "{CALL ReturnBook(?)}";
            CallableStatement callStmt = connect.prepareCall(sql);
            callStmt.setInt(1, selected.getEntryId());
            callStmt.execute();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully returned the book!");
            alert.showAndWait();

            showCurrentBorrows();
            loadAvailableBooks();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while returning the book: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void clearBorrowFields() {
        borrower_combo.setValue(null);
        book_combo.setValue(null);
        borrow_tableView.getSelectionModel().clearSelection();
    }

    public void logout() {
        try {
            logout_btn.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/view/adminLogin.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minimize() {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }
    
    public void exit() {
        System.exit(0);
    }

    public void addBook() {
        String callProc = "CALL AddOrUpdateBook(?, ?)";
        connect = DBConnect.connectDB();
        try {
            Alert alert;
            if (book_title.getText().isEmpty() || book_totalCopies.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(callProc);
                prepare.setString(1, book_title.getText());
                prepare.setInt(2, Integer.parseInt(book_totalCopies.getText()));
                prepare.execute();
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully added or updated book!");
                alert.showAndWait();
                showBooks();
                clearBookFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while adding book");
            alert.showAndWait();
        }
    }

    public void updateBook() {
        String sqlBook = "UPDATE Book SET Title = ?, TotalCopies = ? WHERE BookID = ?";
        String sqlDeleteAuthors = "DELETE FROM BookAuthor WHERE BookID = ?";
        String sqlAuthor = "INSERT INTO Author (FullName) VALUES (?) ON DUPLICATE KEY UPDATE AuthorID=LAST_INSERT_ID(AuthorID)";
        String sqlBookAuthor = "INSERT INTO BookAuthor (BookID, AuthorID) VALUES (?, ?)";
        String sqlDeleteGenres = "DELETE FROM BookGenre WHERE BookID = ?";
        String sqlGenre = "INSERT INTO Genre (Name) VALUES (?) ON DUPLICATE KEY UPDATE GenreID=LAST_INSERT_ID(GenreID)";
        String sqlBookGenre = "INSERT INTO BookGenre (BookID, GenreID) VALUES (?, ?)";
        
        connect = DBConnect.connectDB();
        
        try {
            Alert alert;
            
            availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to update");
                alert.showAndWait();
                return;
            }

            // Nếu tất cả đều trống thì báo lỗi
            if (book_title.getText().isEmpty() && book_author.getText().isEmpty() && book_totalCopies.getText().isEmpty() && book_genre.getText().isEmpty() && book_quantity.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill at least one field to update");
                alert.showAndWait();
                return;
            }

            // Start transaction
            connect.setAutoCommit(false);
            
            try {
                // Lấy giá trị cũ nếu trường để trống
                String newTitle = book_title.getText().isEmpty() ? selectedBook.getTitle() : book_title.getText();
                int newTotalCopies = book_totalCopies.getText().isEmpty() ? selectedBook.getTotalCopies() : Integer.parseInt(book_totalCopies.getText());
                // Không cập nhật availableCopies nữa

                // Update book details (chỉ cập nhật Title và TotalCopies)
                prepare = connect.prepareStatement(sqlBook);
                prepare.setString(1, newTitle);
                prepare.setInt(2, newTotalCopies);
                prepare.setInt(3, selectedBook.getBookId());
                prepare.executeUpdate();

                // Update authors nếu có nhập
                if (!book_author.getText().isEmpty()) {
                    // Delete existing author relationships
                    prepare = connect.prepareStatement(sqlDeleteAuthors);
                    prepare.setInt(1, selectedBook.getBookId());
                    prepare.executeUpdate();

                    // Add new authors
                    String[] authors = book_author.getText().split(",");
                    for (String authorName : authors) {
                        authorName = authorName.trim();
                        if (!authorName.isEmpty()) {
                            // Insert or get existing author
                            prepare = connect.prepareStatement(sqlAuthor, Statement.RETURN_GENERATED_KEYS);
                            prepare.setString(1, authorName);
                            prepare.executeUpdate();
                            
                            // Get author ID
                            ResultSet authorKeys = prepare.getGeneratedKeys();
                            if (authorKeys.next()) {
                                int authorId = authorKeys.getInt(1);
                                // Link book with author
                                prepare = connect.prepareStatement(sqlBookAuthor);
                                prepare.setInt(1, selectedBook.getBookId());
                                prepare.setInt(2, authorId);
                                prepare.executeUpdate();
                            }
                        }
                    }
                }

                // Update genres nếu có nhập
                if (!book_genre.getText().isEmpty()) {
                    // Delete existing genre relationships
                    prepare = connect.prepareStatement(sqlDeleteGenres);
                    prepare.setInt(1, selectedBook.getBookId());
                    prepare.executeUpdate();

                    // Add new genres
                    String[] genres = book_genre.getText().split(",");
                    for (String genreName : genres) {
                        genreName = genreName.trim();
                        if (!genreName.isEmpty()) {
                            // Insert or get existing genre
                            prepare = connect.prepareStatement(sqlGenre, Statement.RETURN_GENERATED_KEYS);
                            prepare.setString(1, genreName);
                            prepare.executeUpdate();
                            // Get genre ID
                            ResultSet genreKeys = prepare.getGeneratedKeys();
                            if (genreKeys.next()) {
                                int genreId = genreKeys.getInt(1);
                                // Link book with genre
                                prepare = connect.prepareStatement(sqlBookGenre);
                                prepare.setInt(1, selectedBook.getBookId());
                                prepare.setInt(2, genreId);
                                prepare.executeUpdate();
                            }
                        }
                    }
                }

                connect.commit();
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully updated book!");
                alert.showAndWait();
                
                showBooks();
                clearBookFields();
            } catch (Exception e) {
                connect.rollback();
                throw e;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while updating book");
            alert.showAndWait();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteBook() {
        String sql = "DELETE FROM Book WHERE BookID = ?";
        
        connect = DBConnect.connectDB();
        
        try {
            Alert alert;
            
            availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to delete");
                alert.showAndWait();
                return;
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this book?");
            
            Optional<ButtonType> option = alert.showAndWait();
            
            if (option.get().equals(ButtonType.OK)) {
                // Note: BookAuthor records will be automatically deleted due to CASCADE
                prepare = connect.prepareStatement(sql);
                prepare.setInt(1, selectedBook.getBookId());
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted book!");
                alert.showAndWait();

                showBooks();
                clearBookFields();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while deleting book");
            alert.showAndWait();
        }
    }

    public void clearBookFields() {
        book_title.setText("");
        book_author.setText("");
        book_totalCopies.setText("");
        getData.bookId = null;
    }

    // Add event handler for table selection
    public void selectBook() {
        availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
        
        if (selectedBook != null) {
            book_title.setText(selectedBook.getTitle());
            book_author.setText(selectedBook.getAuthors());
            book_genre.setText(selectedBook.getGenres());
            book_totalCopies.setText(String.valueOf(selectedBook.getTotalCopies()));
        }
    }

    public ObservableList<UserAccount> usersList(String searchTerm) {
        ObservableList<UserAccount> list = FXCollections.observableArrayList();
        String sql = "SELECT ua.*, b.FullName as BorrowerName " +
                    "FROM UserAccount ua " +
                    "LEFT JOIN Borrower b ON ua.BorrowerID = b.BorrowerID ";
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += "WHERE LOWER(ua.Username) LIKE ? OR LOWER(b.FullName) LIKE ? OR LOWER(ua.BorrowerID) LIKE ? ";
        }
        sql += "ORDER BY ua.AccountID";
        connect = DBConnect.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String pattern = "%" + searchTerm.trim().toLowerCase() + "%";
                prepare.setString(1, pattern);
                prepare.setString(2, pattern);
                prepare.setString(3, pattern);
            }
            result = prepare.executeQuery();
            while (result.next()) {
                list.add(new UserAccount(
                    result.getInt("AccountID"),
                    result.getString("Username"),
                    result.getString("Password"),
                    result.getString("BorrowerID"),
                    result.getString("BorrowerName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void showUsers() {
        showUsers("");
    }

    public void showUsers(String searchTerm) {
        ObservableList<UserAccount> list = usersList(searchTerm);
        col_accountId.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_borrowerId.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        col_borrowerName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        users_tableView.setItems(list);
    }

    @FXML
    public void searchUsers() {
        String searchTerm = user_search_field.getText();
        showUsers(searchTerm);
    }

    @FXML
    public void clearUserSearch() {
        user_search_field.clear();
        showUsers("");
    }

    private void loadAvailableBorrowers() {
        String sql = "SELECT BorrowerID, FullName FROM Borrower ORDER BY FullName";
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            ObservableList<Borrower> borrowers = FXCollections.observableArrayList();
            while (result.next()) {
                borrowers.add(new Borrower(
                    result.getString("BorrowerID"),
                    result.getString("FullName")
                ));
            }
            
            // Set items for borrower combo in borrow records
            if (borrower_combo != null) {
                borrower_combo.setItems(borrowers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUserTypeComboBox() {
        userType_combo.setItems(FXCollections.observableArrayList("SV", "ND"));
        userType_combo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // No need to load borrowers anymore since we removed the borrower_field
            }
        });
    }

    public void addUser() {
        String sql = "INSERT INTO UserAccount (Username, Password, BorrowerID) VALUES (?, ?, ?)";
        String sqlBorrower = "INSERT INTO Borrower (BorrowerID, FullName, IsStudent) VALUES (?, ?, ?)";
        String getLastId = "SELECT BorrowerID FROM Borrower WHERE BorrowerID LIKE ? ORDER BY BorrowerID DESC LIMIT 1";
        
        connect = DBConnect.connectDB();
        
        try {
            Alert alert;
            
            if (username_field.getText().isEmpty() || 
                password_field.getText().isEmpty() || 
                userType_combo.getValue() == null ||
                borrower_name.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all required fields and select user type");
                alert.showAndWait();
                return;
            }

            // Check if username already exists
            String check = "SELECT Username FROM UserAccount WHERE Username = ?";
            prepare = connect.prepareStatement(check);
            prepare.setString(1, username_field.getText());
            result = prepare.executeQuery();
            
            if (result.next()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists!");
                alert.showAndWait();
                return;
            }

            connect.setAutoCommit(false);
            try {
                // Create new borrower
                boolean isStudent = userType_combo.getValue().equals("SV");
                String prefix = isStudent ? "SV" : "ND";
                
                // Get last ID with same prefix
                prepare = connect.prepareStatement(getLastId);
                prepare.setString(1, prefix + "%");
                result = prepare.executeQuery();
                
                int nextNum = 1;
                if (result.next()) {
                    String lastId = result.getString("BorrowerID");
                    String numStr = lastId.substring(2); // Remove prefix
                    nextNum = Integer.parseInt(numStr) + 1;
                }
                
                // Format: SV001 or ND001 (3 digits, padded with zeros)
                String borrowerId = String.format("%s%03d", prefix, nextNum);
                
                prepare = connect.prepareStatement(sqlBorrower);
                prepare.setString(1, borrowerId);
                prepare.setString(2, borrower_name.getText());
                prepare.setBoolean(3, isStudent);
                prepare.executeUpdate();

                // Add new user
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username_field.getText());
                prepare.setString(2, password_field.getText());
                prepare.setString(3, borrowerId);
                prepare.executeUpdate();

                connect.commit();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully added user!");
                alert.showAndWait();

                showUsers();
                clearUserFields();
            } catch (Exception e) {
                connect.rollback();
                throw e;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while adding user");
            alert.showAndWait();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUser() {
        String sql = "UPDATE UserAccount SET Username = ?, Password = ? WHERE AccountID = ?";
        
        connect = DBConnect.connectDB();
        
        try {
            Alert alert;
            
            UserAccount selectedUser = users_tableView.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a user to update");
                alert.showAndWait();
                return;
            }

            if (username_field.getText().isEmpty() || password_field.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all required fields");
                alert.showAndWait();
                return;
            }

            // Check if username already exists for other users
            String check = "SELECT Username FROM UserAccount WHERE Username = ? AND AccountID != ?";
            prepare = connect.prepareStatement(check);
            prepare.setString(1, username_field.getText());
            prepare.setInt(2, selectedUser.getAccountId());
            result = prepare.executeQuery();
            
            if (result.next()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists!");
                alert.showAndWait();
                return;
            }

            // Update user
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username_field.getText());
            prepare.setString(2, password_field.getText());
            prepare.setInt(3, selectedUser.getAccountId());
            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully updated user!");
            alert.showAndWait();

            showUsers();
            clearUserFields();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error updating user: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void deleteUser() {
        String sql = "DELETE FROM UserAccount WHERE AccountID = ?";
        
        connect = DBConnect.connectDB();
        
        try {
            Alert alert;
            
            UserAccount selectedUser = users_tableView.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a user to delete");
                alert.showAndWait();
                return;
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this user?");
            
            Optional<ButtonType> option = alert.showAndWait();
            
            if (option.get().equals(ButtonType.OK)) {
                prepare = connect.prepareStatement(sql);
                prepare.setInt(1, selectedUser.getAccountId());
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted user!");
                alert.showAndWait();

                showUsers();
                loadAvailableBorrowers();
                clearUserFields();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearUserFields() {
        username_field.setText("");
        password_field.setText("");
        userType_combo.setValue(null);
        borrower_name.setText("");
        users_tableView.getSelectionModel().clearSelection();
    }

    // Add selection listener for users table
    private void setupUserTableListener() {
        users_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                username_field.setText(newSelection.getUsername());
                password_field.setText(newSelection.getPassword());
                borrower_name.setText(newSelection.getBorrowerName());
            }
        });
    }

    public ObservableList<Reservation> reservationsList() {
        ObservableList<Reservation> listReservations = FXCollections.observableArrayList();
        String sql = "SELECT r.ReservationID, ua.Username, b.Title, r.ReservationDate, r.Status " +
                    "FROM Reservation r " +
                    "JOIN UserAccount ua ON r.AccountID = ua.AccountID " +
                    "JOIN Book b ON r.BookID = b.BookID " +
                    "ORDER BY r.ReservationDate DESC";
        
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                Reservation reservation = new Reservation(
                    result.getInt("ReservationID"),
                    result.getString("Username"),
                    result.getString("Title"),
                    result.getTimestamp("ReservationDate").toLocalDateTime(),
                    result.getString("Status")
                );
                listReservations.add(reservation);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listReservations;
    }

    public void showReservations() {
        ObservableList<Reservation> listReservations = reservationsList();
        
        col_reservationId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_reservationUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_reservationBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_reservationDate.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        
        // Set up custom cell factory for reservation date
        col_reservationDate.setCellFactory(column -> {
            return new TableCell<Reservation, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                    }
                }
            };
        });
        
        col_reservationStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        reservations_tableView.setItems(listReservations);
    }

    @FXML
    public void updateReservationStatus() {
        Reservation selectedReservation = reservations_tableView.getSelectionModel().getSelectedItem();
        String newStatus = selectedReservationStatus;
        
        if (selectedReservation == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a reservation to update");
            alert.showAndWait();
            return;
        }
        
        if (newStatus == null || newStatus.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a status");
            alert.showAndWait();
            return;
        }

        if (newStatus.equals("Fulfilled")) {
            // Gọi procedure ApproveReservation
            String callProc = "CALL ApproveReservation(?)";
            connect = DBConnect.connectDB();
            try {
                prepare = connect.prepareStatement(callProc);
                prepare.setInt(1, selectedReservation.getId());
                prepare.execute();

                // Sau khi procedure thành công, thêm bản ghi vào BorrowEntry
                // Lấy AccountID, BookID từ reservation
                String getInfo = "SELECT r.BookID, ua.BorrowerID FROM Reservation r JOIN UserAccount ua ON r.AccountID = ua.AccountID WHERE r.ReservationID = ?";
                PreparedStatement ps = connect.prepareStatement(getInfo);
                ps.setInt(1, selectedReservation.getId());
                ResultSet rs = ps.executeQuery();
                int bookId = -1;
                if (rs.next()) {
                    bookId = rs.getInt("BookID");
                    String borrowerId = rs.getString("BorrowerID");
                    String insertBorrow = "INSERT INTO BorrowEntry (BorrowerID, BookID, BorrowDate, ProcessedBy) VALUES (?, ?, NOW(), ?)";
                    PreparedStatement ps2 = connect.prepareStatement(insertBorrow);
                    ps2.setString(1, borrowerId);
                    ps2.setInt(2, bookId);
                    ps2.setInt(3, getData.adminId);
                    ps2.executeUpdate();
                    ps2.close();
                }
                rs.close();
                ps.close();

                // Lấy thêm tên sách (title)
                String getTitle = "SELECT Title FROM Book WHERE BookID = ?";
                PreparedStatement titleStmt = connect.prepareStatement(getTitle);
                titleStmt.setInt(1, bookId);
                ResultSet titleRs = titleStmt.executeQuery();
                String bookTitle = "";
                if (titleRs.next()) {
                    bookTitle = titleRs.getString("Title");
                }
                titleRs.close();
                titleStmt.close();

                // Gửi notification cho user được duyệt
                String notifAccepted = "INSERT INTO Notification(AccountID, Message, DateCreated, IsRead) VALUES (?, ?, NOW(), 0)";
                PreparedStatement notifStmt = connect.prepareStatement(notifAccepted);
                // Lấy AccountID từ reservation
                String getAccountId = "SELECT AccountID FROM Reservation WHERE ReservationID = ?";
                PreparedStatement accStmt = connect.prepareStatement(getAccountId);
                accStmt.setInt(1, selectedReservation.getId());
                ResultSet accRs = accStmt.executeQuery();
                if (accRs.next()) {
                    int accountId = accRs.getInt("AccountID");
                    notifStmt.setInt(1, accountId);
                    notifStmt.setString(2, "Your reservation for '" + bookTitle + "' has been approved and fulfilled.");
                    notifStmt.executeUpdate();
                }
                accRs.close();
                accStmt.close();
                notifStmt.close();

                // Gửi notification cho các user bị canceled (nếu có)
                String notifCancel = "INSERT INTO Notification(AccountID, Message, DateCreated, IsRead) " +
                    "SELECT AccountID, CONCAT('Your reservation for ''', ?, ''' has been canceled due to lack of available copies.'), NOW(), 0 " +
                    "FROM Reservation WHERE BookID = ? AND Status = 'Canceled' AND ReservationDate >= (SELECT ReservationDate FROM Reservation WHERE ReservationID = ?)";
                PreparedStatement notifCancelStmt = connect.prepareStatement(notifCancel);
                notifCancelStmt.setString(1, bookTitle);
                notifCancelStmt.setInt(2, bookId);
                notifCancelStmt.setInt(3, selectedReservation.getId());
                notifCancelStmt.executeUpdate();
                notifCancelStmt.close();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Reservation fulfilled, borrow record created!");
                alert.showAndWait();
                showReservations();
                showBorrowRecords();
                showCurrentBorrows();
                clearReservationFields();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while processing the reservation: " + e.getMessage());
                alert.showAndWait();
            }
            return;
        } else {
            // Just update status for non-Fulfilled statuses
            String sql = "UPDATE Reservation SET Status = ? WHERE ReservationID = ?";
            connect = DBConnect.connectDB();
            
            try {
                Alert alert;
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, newStatus);
                prepare.setInt(2, selectedReservation.getId());
                
                prepare.executeUpdate();

                // Nếu là Canceled thì gửi thông báo
                if (newStatus.equals("Canceled")) {
                    // Lấy AccountID
                    String getAccountId = "SELECT ua.AccountID FROM UserAccount ua WHERE ua.Username = ?";
                    PreparedStatement ps = connect.prepareStatement(getAccountId);
                    ps.setString(1, selectedReservation.getUsername());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int accountId = rs.getInt("AccountID");
                        String message = "Your reservation for '" + selectedReservation.getBookTitle() + "' has been canceled.";
                        String insertNotif = "INSERT INTO Notification (AccountID, Message, DateCreated, IsRead) VALUES (?, ?, NOW(), 0)";
                        PreparedStatement ps2 = connect.prepareStatement(insertNotif);
                        ps2.setInt(1, accountId);
                        ps2.setString(2, message);
                        ps2.executeUpdate();
                        ps2.close();
                    }
                    rs.close();
                    ps.close();
                }
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Reservation status updated successfully!");
                alert.showAndWait();
                
                showReservations();
                clearReservationFields();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void clearReservationFields() {
        reservations_tableView.getSelectionModel().clearSelection();
        selectedReservationStatus = null;
        fulfilled_btn.setStyle("");
        cancel_btn.setStyle("");
    }

    public ObservableList<CurrentBorrow> currentBorrowsList() {
        ObservableList<CurrentBorrow> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM View_CurrentBorrows ORDER BY BorrowDate DESC";
        
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                list.add(new CurrentBorrow(
                    result.getInt("EntryID"),
                    result.getString("BorrowerID"),
                    result.getString("BorrowerName"),
                    result.getString("BookTitle"),
                    result.getTimestamp("BorrowDate").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public void showCurrentBorrows() {
        ObservableList<CurrentBorrow> list = currentBorrowsList();
        
        col_currentBorrowId.setCellValueFactory(new PropertyValueFactory<>("entryId"));
        col_currentBorrowerId.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        col_currentBorrowerName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        col_currentBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_currentBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        
        // Set up date/time format for borrow date column
        col_currentBorrowDate.setCellFactory(column -> {
            return new TableCell<CurrentBorrow, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                    }
                }
            };
        });
        
        currentBorrows_tableView.setItems(list);
    }

    @FXML
    private void setFulfilledStatus() {
        selectedReservationStatus = "Fulfilled";
        fulfilled_btn.setStyle("-fx-border-color: #1976D2; -fx-border-width: 2px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancel_btn.setStyle("");
    }

    @FXML
    private void setCancelStatus() {
        selectedReservationStatus = "Canceled";
        cancel_btn.setStyle("-fx-border-color: #1976D2; -fx-border-width: 2px; -fx-background-color: #F44336; -fx-text-fill: white;");
        fulfilled_btn.setStyle("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showBooks();
        showUsers();
        showBorrowRecords();
        showReservations();
        showCurrentBorrows();
        
        setupUserTypeComboBox();
        setupUserTableListener();
        loadBorrowers();
        loadAvailableBooks();
        
        // Set up date/time format for columns
        col_borrowDate.setCellFactory(column -> {
            return new TableCell<BorrowEntry, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                    }
                }
            };
        });
        
        col_returnDate.setCellFactory(column -> {
            return new TableCell<BorrowEntry, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                    }
                }
            };
        });
        
        col_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        col_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        if (searchTypeCombo != null) {
            searchTypeCombo.setItems(FXCollections.observableArrayList("Title", "Author", "Genre"));
            searchTypeCombo.setValue("Title");
        }

        // Khi chọn dòng trong bảng sách, tự động hiển thị thông tin vào các TextField
        books_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectBook();
            }
        });
    }
} 