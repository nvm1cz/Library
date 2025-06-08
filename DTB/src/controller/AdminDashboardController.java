package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableColumn<availableBooks, Integer> col_bookId;

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
    private TextField book_id;
    
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

        String sql = """
            SELECT 
                b.BookID,
                b.Title,
                auth.Authors,
                gen.Genres,
                b.TotalCopies,
                b.AvailableCopies,
                b.TotalBorrows
            FROM Book b
            LEFT JOIN (
                SELECT ba.BookID, STRING_AGG(a.Name, ', ') AS Authors
                FROM BookAuthor ba
                JOIN Author a ON ba.AuthorID = a.AuthorID
                GROUP BY ba.BookID
            ) auth ON b.BookID = auth.BookID
            LEFT JOIN (
                SELECT bg.BookID, STRING_AGG(g.Name, ', ') AS Genres
                FROM BookGenre bg
                JOIN Genre g ON bg.GenreID = g.GenreID
                GROUP BY bg.BookID
            ) gen ON b.BookID = gen.BookID
            WHERE 1=1
        """;

        boolean hasSearch = searchTerm != null && !searchTerm.trim().isEmpty();
        if (hasSearch && searchType != null) {
            switch (searchType) {
                case "Title":
                    sql += " AND LOWER(b.Title) LIKE ?";
                    break;
                case "Author":
                    sql += " AND LOWER(auth.Authors) LIKE ?";
                    break;
                case "Genre":
                    sql += " AND LOWER(gen.Genres) LIKE ?";
                    break;
                default:
                    sql += " AND (LOWER(b.Title) LIKE ? OR LOWER(auth.Authors) LIKE ? OR LOWER(gen.Genres) LIKE ?)";
            }
        }

        connect = DBConnect.connectDB();
        try {
            prepare = connect.prepareStatement(sql);

            if (hasSearch && searchType != null) {
                String searchPattern = "%" + searchTerm.trim().toLowerCase() + "%";
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
                    result.getInt("TotalBorrows")
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
        col_bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
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
                    result.getInt("TotalBorrows")
                    //0.0 // avgRating mặc định cho combobox
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
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a borrow record to return.");
            alert.showAndWait();
            return;
        }

        Connection connect = null;
        PreparedStatement checkStmt = null;
        CallableStatement callStmt = null;
        ResultSet result = null;

        try {
            connect = DBConnect.connectDB();

            // Optional: Check again in DB if the book is still not returned
            String checkSql = "SELECT 1 FROM BorrowEntry WHERE EntryID = ? AND ReturnDate IS NULL";
            checkStmt = connect.prepareStatement(checkSql);
            checkStmt.setInt(1, selected.getEntryId());
            result = checkStmt.executeQuery();

            if (!result.next()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Already Returned");
                alert.setHeaderText(null);
                alert.setContentText("This book has already been returned.");
                alert.showAndWait();
                return;
            }

            // Call stored procedure
            String callSql = "{CALL returnBook(?)}";
            callStmt = connect.prepareCall(callSql);
            callStmt.setInt(1, selected.getEntryId());
            callStmt.execute();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Book returned successfully!");
            alert.showAndWait();

            showCurrentBorrows();   // refresh table
            loadAvailableBooks();   // update availability if needed

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Return Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to return book: " + e.getMessage());
            alert.showAndWait();
        } finally {
            // Clean up resources
            try {
                if (result != null) result.close();
                if (checkStmt != null) checkStmt.close();
                if (callStmt != null) callStmt.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        String callProc = "EXEC AddBook ?, ?, ?";
        connect = DBConnect.connectDB();
        try {
            Alert alert;

            if (book_title.getText().isEmpty() || book_totalCopies.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
                return;
            }

            int copiesToAdd;
            try {
                copiesToAdd = Integer.parseInt(book_totalCopies.getText());
            } catch (NumberFormatException e) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Copies must be a valid number");
                alert.showAndWait();
                return;
            }

            prepare = connect.prepareStatement(callProc);

            // Handle nullable Book ID
            if (book_id.getText().isEmpty()) {
                prepare.setNull(1, java.sql.Types.INTEGER);
            } else {
                prepare.setInt(1, Integer.parseInt(book_id.getText()));
            }

            prepare.setString(2, book_title.getText());
            prepare.setInt(3, copiesToAdd);

            prepare.execute();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added or updated book!");
            alert.showAndWait();

            showBooks();
            clearBookFields();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while adding book");
            alert.showAndWait();
        }
    }


    public void updateBook() {
        connect = DBConnect.connectDB();

        String updateTitleProc = "{CALL UpdateBook(?, ?)}";
        String deleteAuthorsSql = "DELETE FROM BookAuthor WHERE BookID = ?";
        String deleteGenresSql = "DELETE FROM BookGenre WHERE BookID = ?";
        String insertAuthorProc = "{CALL AddAuthor(?)}";
        String insertGenreProc = "{CALL AddGenre(?)}";
        String getAuthorId = "SELECT AuthorID FROM Author WHERE Name = ?";
        String getGenreId = "SELECT GenreID FROM Genre WHERE Name = ?";
        String addBookAuthorProc = "{CALL AddBookAuthor(?, ?)}";
        String addBookGenreProc = "{CALL AddBookGenre(?, ?)}";

        try {
            Alert alert;

            availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                alert = new Alert(AlertType.ERROR, "Please select a book to update");
                alert.showAndWait();
                return;
            }

            if (book_title.getText().isEmpty() && book_author.getText().isEmpty() && book_genre.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR, "Please fill at least one field to update");
                alert.showAndWait();
                return;
            }

            connect.setAutoCommit(false);

            int bookId = selectedBook.getBookId();

            // Update title
            if (!book_title.getText().isEmpty()) {
                prepare = connect.prepareCall(updateTitleProc);
                prepare.setInt(1, bookId);
                prepare.setString(2, book_title.getText().trim());
                prepare.execute();
            }

            // Update authors
            if (!book_author.getText().isEmpty()) {
                // Delete current
                prepare = connect.prepareStatement(deleteAuthorsSql);
                prepare.setInt(1, bookId);
                prepare.executeUpdate();

                String[] authors = book_author.getText().split(",");
                for (String authorName : authors) {
                    authorName = authorName.trim();
                    if (!authorName.isEmpty()) {
                        // Insert if not exists
                        prepare = connect.prepareCall(insertAuthorProc);
                        prepare.setString(1, authorName);
                        try { prepare.execute(); } catch (Exception ignored) {}

                        // Get AuthorID
                        prepare = connect.prepareStatement(getAuthorId);
                        prepare.setString(1, authorName);
                        ResultSet rs = prepare.executeQuery();
                        if (rs.next()) {
                            int authorId = rs.getInt("AuthorID");
                            prepare = connect.prepareCall(addBookAuthorProc);
                            prepare.setInt(1, bookId);
                            prepare.setInt(2, authorId);
                            prepare.execute();
                        }
                    }
                }
            }

            // Update genres
            if (!book_genre.getText().isEmpty()) {
                prepare = connect.prepareStatement(deleteGenresSql);
                prepare.setInt(1, bookId);
                prepare.executeUpdate();

                String[] genres = book_genre.getText().split(",");
                for (String genreName : genres) {
                    genreName = genreName.trim();
                    if (!genreName.isEmpty()) {
                        prepare = connect.prepareCall(insertGenreProc);
                        prepare.setString(1, genreName);
                        try { prepare.execute(); } catch (Exception ignored) {}

                        prepare = connect.prepareStatement(getGenreId);
                        prepare.setString(1, genreName);
                        ResultSet rs = prepare.executeQuery();
                        if (rs.next()) {
                            int genreId = rs.getInt("GenreID");
                            prepare = connect.prepareCall(addBookGenreProc);
                            prepare.setInt(1, bookId);
                            prepare.setInt(2, genreId);
                            prepare.execute();
                        }
                    }
                }
            }

            connect.commit();

            Alert successAlert = new Alert(AlertType.INFORMATION, "Successfully updated book!");
            successAlert.showAndWait();

            showBooks();
            clearBookFields();

        } catch (Exception e) {
            try {
                connect.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Error occurred while updating book");
            alert.showAndWait();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void deleteBook() {
        String callProc = "{CALL DeleteBook(?)}";

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

            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                CallableStatement stmt = connect.prepareCall(callProc);
                stmt.setInt(1, selectedBook.getBookId());

                int result = stmt.executeUpdate(); // Will be 0 if book not deleted due to procedure logic

                if (result > 0) {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully deleted book!");
                } else {
                    alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book cannot be deleted. It may still be borrowed or not exist.");
                }
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
    	book_id.setText("");
        book_title.setText("");
        book_author.setText("");
        book_totalCopies.setText("");
        getData.bookId = null;
    }

    // Add event handler for table selection
    public void selectBook() {
        availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
        
        if (selectedBook != null) {
        	book_id.setText(String.valueOf(selectedBook.getBookId()));
            book_title.setText(selectedBook.getTitle());
            book_author.setText(selectedBook.getAuthors());
            book_genre.setText(selectedBook.getGenres());
            book_totalCopies.setText(String.valueOf(selectedBook.getTotalCopies()));
        }
    }

    public ObservableList<UserAccount> usersList(String searchTerm) {
        ObservableList<UserAccount> list = FXCollections.observableArrayList();
        
        String sql = "SELECT b.BorrowerID, b.FullName, ua.Username " +
                     "FROM Borrower b " +
                     "LEFT JOIN UserAccount ua ON b.BorrowerID = ua.BorrowerID ";

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += "WHERE LOWER(ISNULL(ua.Username, '')) LIKE ? " +
                   "OR LOWER(b.FullName) LIKE ? " +
                   "OR LOWER(b.BorrowerID) LIKE ? ";
        }

        sql += "ORDER BY b.BorrowerID";

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
                    result.getString("BorrowerID"),
                    result.getString("Username"),  // will be null if not created
                    null                            // do not expose password
                    // optionally add FullName if your UserAccount class has it
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
            //prepare.setInt(2, selectedUser.getAccountId());
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
            //prepare.setInt(3, selectedUser.getAccountId());
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
                //prepare.setInt(1, selectedUser.getAccountId());
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
                     "LEFT JOIN UserAccount ua ON r.BorrowerID = ua.BorrowerID " +
                     "JOIN Book b ON r.BookID = b.BookID " +
                     "ORDER BY r.ReservationDate DESC";

        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                Reservation reservation = new Reservation(
                    result.getInt("ReservationID"),
                    result.getString("Username") != null ? result.getString("Username") : "[No Account]",
                    result.getString("Title"),
                    result.getDate("ReservationDate").toLocalDate().atStartOfDay(),
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
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a reservation to update");
            return;
        }

        if (newStatus == null || newStatus.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a status");
            return;
        }

        connect = DBConnect.connectDB();

        try {
            String status = selectedReservation.getStatus();

            if (newStatus.equalsIgnoreCase("Fulfilled")) {
                if ("Pending".equalsIgnoreCase(status)) {
                    // Call fulfilReservation
                    String callProc = "EXEC fulfilReservation ?";
                    prepare = connect.prepareStatement(callProc);
                    prepare.setInt(1, selectedReservation.getId());
                    prepare.execute();

                    showAlert(Alert.AlertType.INFORMATION, "Fulfilled", "Reservation is now marked as fulfilled.");
                } else if ("Fulfilled".equalsIgnoreCase(status)) {
                    // Call completeReservation
                    String callProc = "EXEC completeReservation ?, ?";
                    prepare = connect.prepareStatement(callProc);
                    prepare.setInt(1, selectedReservation.getId());
                    prepare.setInt(2, getData.adminId);
                    prepare.execute();

                    showAlert(Alert.AlertType.INFORMATION, "Completed", "Reservation completed and book borrowed.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Invalid Operation", "Reservation must be Pending or Fulfilled.");
                    return;
                }

            } else if (newStatus.equalsIgnoreCase("Canceled")) {
                if ("Pending".equalsIgnoreCase(status)) {
                    // Cancel pending reservation
                    String cancelProc = "EXEC cancelPendingReservation ?";
                    prepare = connect.prepareStatement(cancelProc);
                    prepare.setInt(1, selectedReservation.getId());
                    prepare.execute();

                    showAlert(Alert.AlertType.INFORMATION, "Canceled", "Pending reservation canceled successfully.");

                } else if ("Fulfilled".equalsIgnoreCase(status)) {
                    // Cancel fulfilled reservation
                    String cancelProc = "EXEC cancelFulfilledReservation ?";
                    prepare = connect.prepareStatement(cancelProc);
                    prepare.setInt(1, selectedReservation.getId());
                    prepare.execute();

                    showAlert(Alert.AlertType.INFORMATION, "Canceled", "Fulfilled reservation canceled and inventory restored.");

                } else {
                    showAlert(Alert.AlertType.WARNING, "Invalid Operation", "Only Pending or Fulfilled reservations can be canceled.");
                    return;
                }

            } else {
                showAlert(Alert.AlertType.WARNING, "Invalid Operation", "Only Fulfilled or Canceled actions are supported.");
                return;
            }

            showReservations();
            showBorrowRecords();
            showCurrentBorrows();
            clearReservationFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating reservation: " + e.getMessage());
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

        // Use explicit columns instead of SELECT *
        String sql = "SELECT EntryID, BorrowerID, BorrowerName, BookTitle, BorrowDate " +
                     "FROM View_CurrentBorrows ORDER BY BorrowDate DESC";

        connect = DBConnect.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                int entryId = result.getInt("EntryID");
                String borrowerId = result.getString("BorrowerID");
                String borrowerName = result.getString("BorrowerName");
                String bookTitle = result.getString("BookTitle");
                LocalDateTime borrowDate = result.getTimestamp("BorrowDate").toLocalDateTime();

                list.add(new CurrentBorrow(entryId, borrowerId, borrowerName, bookTitle, borrowDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Optional: show alert to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading current borrows.");
            alert.showAndWait();
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
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 