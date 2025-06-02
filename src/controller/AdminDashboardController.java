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

import dao.Database;
import model.getData;
import model.BorrowEntry;
import model.availableBooks;
import model.UserAccount;
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
    private ComboBox<Borrower> borrower_field;

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
    private TableColumn<BorrowEntry, Integer> col_borrowId;

    @FXML
    private TableColumn<BorrowEntry, String> col_borrower;

    @FXML
    private TableColumn<BorrowEntry, String> col_book;

    @FXML
    private TableColumn<BorrowEntry, LocalDate> col_borrowDate;

    @FXML
    private TableColumn<BorrowEntry, LocalDate> col_returnDate;

    @FXML
    private TableColumn<BorrowEntry, String> col_status;

    @FXML
    private ComboBox<Borrower> borrower_combo;

    @FXML
    private ComboBox<availableBooks> book_combo;

    @FXML
    private DatePicker returnDate_picker;

    @FXML
    private Button addBorrow_btn;

    @FXML
    private Button returnBook_btn;

    @FXML
    private Button clearBorrow_btn;

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
            showBooks();
        } else if (event.getSource() == manageUsers_btn) {
            manageBooks_form.setVisible(false);
            manageUsers_form.setVisible(true);
            borrowRecords_form.setVisible(false);
            showUsers();
            loadAvailableBorrowers();
        } else if (event.getSource() == borrowRecords_btn) {
            manageBooks_form.setVisible(false);
            manageUsers_form.setVisible(false);
            borrowRecords_form.setVisible(true);
            showBorrowRecords();
            loadBorrowers();
            loadAvailableBooks();
        }
    }

    public ObservableList<availableBooks> booksList() {
        ObservableList<availableBooks> bookList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Book";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            availableBooks book;
            
            while (result.next()) {
                book = new availableBooks(
                        result.getInt("BookID"),
                        result.getString("Title"),
                        result.getString("Author"),
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
        ObservableList<availableBooks> listBooks = booksList();
        
        col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_totalCopies.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        col_availableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        col_totalBorrows.setCellValueFactory(new PropertyValueFactory<>("totalBorrows"));
        
        books_tableView.setItems(listBooks);
    }

    public ObservableList<BorrowEntry> borrowList() {
        ObservableList<BorrowEntry> list = FXCollections.observableArrayList();
        String sql = "SELECT be.*, b.FullName as BorrowerName, bk.Title as BookTitle " +
                    "FROM BorrowEntry be " +
                    "JOIN Borrower b ON be.BorrowerID = b.BorrowerID " +
                    "JOIN Book bk ON be.BookID = bk.BookID " +
                    "ORDER BY be.EntryID DESC";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                Date returnDate = result.getDate("ReturnDate");
                list.add(new BorrowEntry(
                    result.getInt("EntryID"),
                    result.getString("BorrowerID"),
                    result.getString("BorrowerName"),
                    result.getInt("BookID"),
                    result.getString("BookTitle"),
                    result.getDate("BorrowDate").toLocalDate(),
                    returnDate != null ? returnDate.toLocalDate() : null,
                    result.getInt("ProcessedBy")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public void showBorrowRecords() {
        ObservableList<BorrowEntry> list = borrowList();
        
        col_borrowId.setCellValueFactory(new PropertyValueFactory<>("entryId"));
        col_borrower.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        col_book.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        col_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        borrow_tableView.setItems(list);
    }

    private void loadBorrowers() {
        String sql = "SELECT BorrowerID, FullName FROM Borrower ORDER BY FullName";
        connect = Database.connectDB();
        
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
            borrower_combo.setItems(borrowers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAvailableBooks() {
        String sql = "SELECT * FROM Book WHERE AvailableCopies > 0 ORDER BY Title";
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            ObservableList<availableBooks> books = FXCollections.observableArrayList();
            while (result.next()) {
                books.add(new availableBooks(
                    result.getInt("BookID"),
                    result.getString("Title"),
                    result.getString("Author"),
                    result.getInt("TotalCopies"),
                    result.getInt("AvailableCopies"),
                    result.getInt("TotalBorrows")
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
        String updateBook = "UPDATE Book SET AvailableCopies = AvailableCopies - 1, TotalBorrows = TotalBorrows + 1 WHERE BookID = ?";
        
        connect = Database.connectDB();
        
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

            connect.setAutoCommit(false);
            try {
                // Add borrow entry
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, borrower_combo.getValue().getId());
                prepare.setInt(2, book_combo.getValue().getBookId());
                prepare.setDate(3, Date.valueOf(LocalDate.now()));
                prepare.setInt(4, getData.adminId);
                prepare.executeUpdate();

                // Update book counts
                prepare = connect.prepareStatement(updateBook);
                prepare.setInt(1, book_combo.getValue().getBookId());
                prepare.executeUpdate();

                connect.commit();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully added borrow record!");
                alert.showAndWait();

                showBorrowRecords();
                loadAvailableBooks();
                clearBorrowFields();
            } catch (Exception e) {
                connect.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while adding borrow record");
            alert.showAndWait();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void returnBook() {
        BorrowEntry selected = borrow_tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a borrow record to return");
            alert.showAndWait();
            return;
        }

        if (selected.getReturnDate() != null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("This book has already been returned");
            alert.showAndWait();
            return;
        }

        String updateBorrow = "UPDATE BorrowEntry SET ReturnDate = ? WHERE EntryID = ?";
        String updateBook = "UPDATE Book SET AvailableCopies = AvailableCopies + 1 WHERE BookID = ?";
        
        connect = Database.connectDB();
        
        try {
            connect.setAutoCommit(false);
            try {
                // Update borrow entry
                prepare = connect.prepareStatement(updateBorrow);
                prepare.setDate(1, Date.valueOf(LocalDate.now()));
                prepare.setInt(2, selected.getEntryId());
                prepare.executeUpdate();

                // Update book counts
                prepare = connect.prepareStatement(updateBook);
                prepare.setInt(1, selected.getBookId());
                prepare.executeUpdate();

                connect.commit();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully returned the book!");
                alert.showAndWait();

                showBorrowRecords();
                loadAvailableBooks();
                clearBorrowFields();
            } catch (Exception e) {
                connect.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while returning the book");
            alert.showAndWait();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearBorrowFields() {
        borrower_combo.setValue(null);
        book_combo.setValue(null);
        returnDate_picker.setValue(null);
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
        String sql = "INSERT INTO Book (Title, Author, TotalCopies, AvailableCopies, TotalBorrows) VALUES (?, ?, ?, ?, 0)";
        
        connect = Database.connectDB();
        
        try {
            Alert alert;
            
            if (book_title.getText().isEmpty() || 
                book_author.getText().isEmpty() || 
                book_totalCopies.getText().isEmpty()) {
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT Title FROM Book WHERE Title = '" + book_title.getText() + "'";
                
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);
                
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book: " + book_title.getText() + " already exists!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, book_title.getText());
                    prepare.setString(2, book_author.getText());
                    
                    int copies = Integer.parseInt(book_totalCopies.getText());
                    prepare.setInt(3, copies);
                    prepare.setInt(4, copies); // Initially available copies equals total copies
                    
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    
                    // TO UPDATE THE TABLEVIEW
                    showBooks();
                    // TO CLEAR THE FIELDS
                    clearBookFields();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBook() {
        String sql = "DELETE FROM Book WHERE BookID = '" + getData.bookId + "'";
        
        connect = Database.connectDB();
        
        try {
            Alert alert;
            
            if (getData.bookId == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to delete");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this book?");
                
                Optional<ButtonType> option = alert.showAndWait();
                
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    
                    // TO UPDATE THE TABLEVIEW
                    showBooks();
                    // TO CLEAR THE FIELDS
                    clearBookFields();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            book_author.setText(selectedBook.getAuthor());
            book_quantity.setText(String.valueOf(selectedBook.getAvailableCopies()));
        }
    }

    public ObservableList<UserAccount> usersList() {
        ObservableList<UserAccount> list = FXCollections.observableArrayList();
        String sql = "SELECT ua.*, b.FullName as BorrowerName " +
                    "FROM UserAccount ua " +
                    "LEFT JOIN Borrower b ON ua.BorrowerID = b.BorrowerID " +
                    "ORDER BY ua.AccountID";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
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
        ObservableList<UserAccount> list = usersList();
        
        col_accountId.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_borrowerId.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        col_borrowerName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        
        users_tableView.setItems(list);
    }

    private void loadAvailableBorrowers() {
        String sql = "SELECT BorrowerID, FullName FROM Borrower " +
                    "WHERE BorrowerID NOT IN (SELECT BorrowerID FROM UserAccount WHERE BorrowerID IS NOT NULL) " +
                    "ORDER BY FullName";
        connect = Database.connectDB();
        
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
            borrower_field.setItems(borrowers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser() {
        String sql = "INSERT INTO UserAccount (Username, Password, BorrowerID) VALUES (?, ?, ?)";
        
        connect = Database.connectDB();
        
        try {
            Alert alert;
            
            if (username_field.getText().isEmpty() || password_field.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all required fields");
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

            // Add new user
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username_field.getText());
            prepare.setString(2, password_field.getText());
            prepare.setString(3, borrower_field.getValue() != null ? borrower_field.getValue().getId() : null);
            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added user!");
            alert.showAndWait();

            showUsers();
            loadAvailableBorrowers();
            clearUserFields();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser() {
        String sql = "UPDATE UserAccount SET Username = ?, Password = ?, BorrowerID = ? WHERE AccountID = ?";
        
        connect = Database.connectDB();
        
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
            prepare.setString(3, borrower_field.getValue() != null ? borrower_field.getValue().getId() : null);
            prepare.setInt(4, selectedUser.getAccountId());
            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully updated user!");
            alert.showAndWait();

            showUsers();
            loadAvailableBorrowers();
            clearUserFields();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        String sql = "DELETE FROM UserAccount WHERE AccountID = ?";
        
        connect = Database.connectDB();
        
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
        borrower_field.setValue(null);
        users_tableView.getSelectionModel().clearSelection();
    }

    // Add selection listener for users table
    private void setupUserTableListener() {
        users_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                username_field.setText(newSelection.getUsername());
                password_field.setText(newSelection.getPassword());
                
                // Find and select the matching borrower in the combo box
                if (newSelection.getBorrowerId() != null) {
                    for (Borrower borrower : borrower_field.getItems()) {
                        if (borrower.getId().equals(newSelection.getBorrowerId())) {
                            borrower_field.setValue(borrower);
                            break;
                        }
                    }
                } else {
                    borrower_field.setValue(null);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBooks();
        setupUserTableListener();
        
        // Add selection listener to books table
        books_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getData.bookId = newSelection.getBookId();
                book_title.setText(newSelection.getTitle());
                book_author.setText(newSelection.getAuthor());
                book_totalCopies.setText(String.valueOf(newSelection.getTotalCopies()));
            }
        });
    }
} 