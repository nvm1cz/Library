package controller;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Action;

import dao.Database;
import model.getData;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.sql.CallableStatement;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;

public class DashBoardController implements Initializable {
    

    @FXML
    private Button close;
    

    @FXML
    private Button minimize;

    @FXML
    private Button bars_btn;

    @FXML
    private Button arrow_btn;

    @FXML
    private AnchorPane nav_form;

    

    @FXML
    private Circle circle_image;

    @FXML
    private Label borrowId_label;

    @FXML
    private Button availableBooks_btn;

    @FXML
    private Button edit_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane availableBooks_form;

    @FXML
    private TableView<availableBooks> availableBooks_tableView;

    @FXML
    private TableColumn<availableBooks, String> col_ab_bookTitle;

    @FXML
    private TableColumn<availableBooks, String> col_ab_author;

    @FXML
    private TableColumn<availableBooks, String> col_ab_genre;

    @FXML
    private TableColumn<availableBooks, Integer> col_ab_availableCopies;

    @FXML
    private Button take_btn;

    @FXML
    private AnchorPane mainCenter_form;

    @FXML
    private Button halfNav_availableBtn;

    @FXML
    private AnchorPane halfNav_form;

    @FXML
    private Circle smallCircle_image;

    @FXML
    private Label currentForm_label;

    @FXML
    private TextField search_field;

    @FXML
    private Button search_btn;

    @FXML
    private Button clear_search_btn;

    @FXML
    private Button notification_btn;

    @FXML
    private Button returnBooks_btn;

    @FXML
    private Button halfNav_returnBtn;

    @FXML
    private AnchorPane returnBooks_form;

    @FXML
    private TableView<BorrowedBook> returnBooks_tableView;

    @FXML
    private TableColumn<BorrowedBook, String> col_rb_bookTitle;

    @FXML
    private TableColumn<BorrowedBook, String> col_rb_author;

    @FXML
    private TableColumn<BorrowedBook, String> col_rb_borrowDate;

    @FXML
    private Label returnBook_title;

    @FXML
    private Label returnBook_author;

    @FXML
    private Label returnBook_borrowDate;

    @FXML
    private Button return_btn;

    @FXML
    private TableView<WishlistBook> wishlist_tableView;

    @FXML
    private TableColumn<WishlistBook, String> col_wl_bookTitle;

    @FXML
    private TableColumn<WishlistBook, String> col_wl_author;

    @FXML
    private TableColumn<WishlistBook, String> col_wl_genre;

    @FXML
    private TableColumn<WishlistBook, String> col_wl_dateAdded;

    @FXML
    private Button favorite_btn;

    @FXML
    private Button remove_wishlist_btn;

    @FXML
    private AnchorPane settings_form;

    @FXML
    private TextField settings_fullname;

    @FXML
    private TextField settings_phone;

    @FXML
    private PasswordField settings_currentPassword;

    @FXML
    private PasswordField settings_newPassword;

    @FXML
    private PasswordField settings_confirmPassword;

    @FXML
    private Button settings_btn;

    @FXML
    private Button borrowedBooks_btn;

    @FXML
    private AnchorPane borrowedBooks_form;

    @FXML
    private TableView<BorrowedBookDisplay> borrowedBooks_tableView;

    @FXML
    private TableColumn<BorrowedBookDisplay, String> col_bb_bookTitle;

    @FXML
    private TableColumn<BorrowedBookDisplay, String> col_bb_author;

    @FXML
    private TableColumn<BorrowedBookDisplay, String> col_bb_borrowDate;

    @FXML
    private TableColumn<BorrowedBookDisplay, String> col_bb_returnDate;

    @FXML
    private TableColumn<BorrowedBookDisplay, String> col_bb_status;

    private Image image;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void displayBorrowerId() {
        borrowId_label.setText("BID: " + getData.borrowerId);
    }

    public void displayDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new java.util.Date());
    }
    //TO SHOW THE BOOKS DATA

    public ObservableList<availableBooks> dataList() {
        return dataList(null);
    }

    public ObservableList<availableBooks> dataList(String searchTerm) {
        ObservableList<availableBooks> listBooks = FXCollections.observableArrayList();
        String sql = "SELECT b.BookID, b.Title, GROUP_CONCAT(DISTINCT a.FullName SEPARATOR ', ') as Authors, " +
                    "GROUP_CONCAT(DISTINCT g.Name SEPARATOR ', ') as Genres, " +
                    "b.TotalCopies, b.AvailableCopies, b.TotalBorrows " +
                    "FROM Book b " +
                    "LEFT JOIN BookAuthor ba ON b.BookID = ba.BookID " +
                    "LEFT JOIN Author a ON ba.AuthorID = a.AuthorID " +
                    "LEFT JOIN BookGenre bg ON b.BookID = bg.BookID " +
                    "LEFT JOIN Genre g ON bg.GenreID = g.GenreID " +
                    "WHERE b.AvailableCopies > 0";
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += " AND (LOWER(b.Title) LIKE LOWER(?) OR LOWER(a.FullName) LIKE LOWER(?) OR LOWER(g.Name) LIKE LOWER(?))";
        }
        
        sql += " GROUP BY b.BookID";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String searchPattern = "%" + searchTerm.trim() + "%";
                prepare.setString(1, searchPattern);
                prepare.setString(2, searchPattern);
                prepare.setString(3, searchPattern);
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
                listBooks.add(book);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listBooks;
    }

    public void showAvailableBooks() {
        ObservableList<availableBooks> listBooks = dataList();
        
        col_ab_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_ab_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        col_ab_genre.setCellValueFactory(new PropertyValueFactory<>("genres"));
        col_ab_availableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        
        availableBooks_tableView.setItems(listBooks);
    }

    public void selectAvailableBooks() {
        availableBooks bookData = availableBooks_tableView.getSelectionModel().getSelectedItem();
        int num = availableBooks_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
    }

    public void abTakeButton(ActionEvent event) {
        if (event.getSource() == take_btn) {
            availableBooks book = availableBooks_tableView.getSelectionModel().getSelectedItem();
            
            if (book == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to reserve.");
                alert.showAndWait();
                return;
            }

            // Confirm reservation
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Reservation");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Do you want to reserve the book: " + book.getTitle() + "?");
            
            if (confirmAlert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                try {
                    connect = Database.connectDB();
                    
                    // First get the AccountID for the current user
                    String getAccountSql = "SELECT AccountID FROM UserAccount WHERE BorrowerID = ?";
                    prepare = connect.prepareStatement(getAccountSql);
                    prepare.setString(1, getData.borrowerId);
                    result = prepare.executeQuery();
                    
                    if (!result.next()) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Could not find your account information.");
                        alert.showAndWait();
                        return;
                    }
                    
                    int accountId = result.getInt("AccountID");

                    // Call stored procedure to check if user can reserve this book
                    String checkSql = "{CALL CheckCanReserveBook(?, ?, ?)}";
                    CallableStatement callStmt = connect.prepareCall(checkSql);
                    callStmt.setInt(1, accountId);
                    callStmt.setInt(2, book.getBookId());
                    callStmt.registerOutParameter(3, java.sql.Types.INTEGER);
                    callStmt.execute();
                    
                    int canReserve = callStmt.getInt(3);
                    
                    if (canReserve == 0) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("You cannot reserve this book because you have already borrowed it and haven't returned it yet.");
                        alert.showAndWait();
                        return;
                    }
                    
                    // Check if user already has a pending reservation for this book
                    String checkReservationSql = "SELECT COUNT(*) as count FROM Reservation " +
                                               "WHERE AccountID = ? AND BookID = ? AND Status = 'Pending'";
                    prepare = connect.prepareStatement(checkReservationSql);
                    prepare.setInt(1, accountId);
                    prepare.setInt(2, book.getBookId());
                    result = prepare.executeQuery();
                    
                    if (result.next() && result.getInt("count") > 0) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("You already have a pending reservation for this book.");
                        alert.showAndWait();
                        return;
                    }
                    
                    // Create reservation
                    String createReservationSql = "INSERT INTO Reservation (AccountID, BookID, ReservationDate, Status) " +
                                                "VALUES (?, ?, CURRENT_TIMESTAMP, 'Pending')";
                    prepare = connect.prepareStatement(createReservationSql);
                    prepare.setInt(1, accountId);
                    prepare.setInt(2, book.getBookId());
                    prepare.executeUpdate();

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book reserved successfully! Please wait for library staff to process your reservation.");
                    alert.showAndWait();

                    // Refresh the available books table
                    showAvailableBooks();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Error creating reservation: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    public void studentNumber() {
        borrowId_label.setText("BID: " + getData.borrowerId);
        
        // Get borrowId for current user from borrower table
        String sql = "SELECT BorrowerID FROM Borrower WHERE BorrowerID = ?";
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            
            if(result.next()) {
                String borrowId = result.getString("BorrowerID");
                borrowId_label.setText(borrowId != null ? borrowId : "--");
            } else {
                borrowId_label.setText("--");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            borrowId_label.setText("--");
        }
    }

    public void sideNavButtonDesign(ActionEvent event) {
        if (event.getSource() == halfNav_availableBtn) {
            availableBooks_form.setVisible(true);
            returnBooks_form.setVisible(false);
            settings_form.setVisible(false);
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            returnBooks_btn.setStyle(null);
            settings_btn.setStyle(null);
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_returnBtn.setStyle(null);
            currentForm_label.setText("Available Books");
        } else if (event.getSource() == halfNav_returnBtn) {
            availableBooks_form.setVisible(false);
            returnBooks_form.setVisible(true);
            settings_form.setVisible(false);
            availableBooks_btn.setStyle(null);
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            settings_btn.setStyle(null);
            halfNav_availableBtn.setStyle(null);
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            currentForm_label.setText("My Wishlist");
            showWishlist();
        } else if (event.getSource() == settings_btn) {
            availableBooks_form.setVisible(false);
            returnBooks_form.setVisible(false);
            settings_form.setVisible(true);
            availableBooks_btn.setStyle(null);
            returnBooks_btn.setStyle(null);
            settings_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle(null);
            halfNav_returnBtn.setStyle(null);
            currentForm_label.setText("Settings");
            loadUserInfo();
        }
    }

    public void navButtonDesign(ActionEvent event) {
        if (event.getSource() == availableBooks_btn) {
            availableBooks_form.setVisible(true);
            borrowedBooks_form.setVisible(false);
            returnBooks_form.setVisible(false);
            settings_form.setVisible(false);
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            borrowedBooks_btn.setStyle(null);
            returnBooks_btn.setStyle(null);
            settings_btn.setStyle(null);
            currentForm_label.setText("Available Books");
        } else if (event.getSource() == borrowedBooks_btn) {
            availableBooks_form.setVisible(false);
            borrowedBooks_form.setVisible(true);
            returnBooks_form.setVisible(false);
            settings_form.setVisible(false);
            availableBooks_btn.setStyle(null);
            borrowedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            returnBooks_btn.setStyle(null);
            settings_btn.setStyle(null);
            currentForm_label.setText("Borrowed Books");
            showAllBorrowedBooks();
        } else if (event.getSource() == returnBooks_btn) {
            availableBooks_form.setVisible(false);
            borrowedBooks_form.setVisible(false);
            returnBooks_form.setVisible(true);
            settings_form.setVisible(false);
            availableBooks_btn.setStyle(null);
            borrowedBooks_btn.setStyle(null);
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            settings_btn.setStyle(null);
            currentForm_label.setText("My Wishlist");
            showWishlist();
        } else if (event.getSource() == settings_btn) {
            availableBooks_form.setVisible(false);
            borrowedBooks_form.setVisible(false);
            returnBooks_form.setVisible(false);
            settings_form.setVisible(true);
            availableBooks_btn.setStyle(null);
            borrowedBooks_btn.setStyle(null);
            returnBooks_btn.setStyle(null);
            settings_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            currentForm_label.setText("Profile");
            loadUserInfo();
        }
    }
    
    private double x = 0;
    private double y = 0;

    public void sliderArrow(){
        nav_form.setVisible(true);
        halfNav_form.setVisible(false);
        arrow_btn.setVisible(false);
        bars_btn.setVisible(true);
    }

    public void sliderBars(){
        nav_form.setVisible(false);
        halfNav_form.setVisible(true);
        arrow_btn.setVisible(true);
        bars_btn.setVisible(false);
    }
    //qua bo doiiii

    @FXML
    public void logout(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {
                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("/view/document.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent e) -> {
                    x = e.getSceneX();
                    y = e.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent e) -> {
                    stage.setX(e.getScreenX() - x);
                    stage.setY(e.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent e) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void exit() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayBorrowerId();
        showAvailableBooks();
        setupSearchField();
        loadUserInfo();
        
        // Initialize wishlist table columns if we're in the wishlist view
        if (wishlist_tableView != null) {
            col_wl_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            col_wl_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
            col_wl_genre.setCellValueFactory(new PropertyValueFactory<>("genres"));
            col_wl_dateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        }
        if (borrowedBooks_tableView != null) {
            col_bb_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            col_bb_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
            col_bb_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
            col_bb_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            col_bb_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    // Add BorrowedBook class
    public class BorrowedBook {
        private int bookId;
        private String title;
        private String authors;
        private String borrowDate;

        public BorrowedBook(int bookId, String title, String authors, String borrowDate) {
            this.bookId = bookId;
            this.title = title;
            this.authors = authors;
            this.borrowDate = borrowDate;
        }

        public int getBookId() { return bookId; }
        public String getTitle() { return title; }
        public String getAuthors() { return authors; }
        public String getBorrowDate() { return borrowDate; }
    }

    public ObservableList<BorrowedBook> getBorrowedBooksList() {
        ObservableList<BorrowedBook> borrowedBooks = FXCollections.observableArrayList();
        
        String sql = "SELECT b.BookID, b.Title, GROUP_CONCAT(a.FullName SEPARATOR ', ') as Authors, br.BorrowDate " +
                    "FROM Book b " +
                    "JOIN BorrowEntry br ON b.BookID = br.BookID " +
                    "JOIN Borrower bo ON br.BorrowerID = bo.BorrowerID " +
                    "LEFT JOIN BookAuthor ba ON b.BookID = ba.BookID " +
                    "LEFT JOIN Author a ON ba.AuthorID = a.AuthorID " +
                    "WHERE bo.BorrowerID = ? AND br.ReturnDate IS NULL " +
                    "GROUP BY b.BookID, br.BorrowDate";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            
            while (result.next()) {
                BorrowedBook book = new BorrowedBook(
                    result.getInt("BookID"),
                    result.getString("Title"),
                    result.getString("Authors"),
                    result.getString("BorrowDate")
                );
                borrowedBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return borrowedBooks;
    }

    public void showBorrowedBooks() {
        ObservableList<BorrowedBook> borrowedBooks = getBorrowedBooksList();
        
        col_rb_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_rb_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        col_rb_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        
        returnBooks_tableView.setItems(borrowedBooks);
    }

    public void selectBorrowedBook() {
        BorrowedBook selectedBook = returnBooks_tableView.getSelectionModel().getSelectedItem();
        int num = returnBooks_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1 || selectedBook == null) {
            return;
        }

        // Only try to update labels if they exist and book is selected
        if (returnBook_title != null && returnBook_author != null && returnBook_borrowDate != null) {
            returnBook_title.setText(selectedBook.getTitle());
            returnBook_author.setText(selectedBook.getAuthors());
            returnBook_borrowDate.setText(selectedBook.getBorrowDate());
        }
    }

    public void returnBook() {
        BorrowedBook selectedBook = returnBooks_tableView.getSelectionModel().getSelectedItem();
        
        if (selectedBook == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to return");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Return");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to return: " + selectedBook.getTitle() + "?");

        if (confirmAlert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            try {
                connect = Database.connectDB();

                // Update return date in BorrowEntry
                String updateBorrowSql = "UPDATE BorrowEntry br " +
                                       "JOIN Borrower b ON br.BorrowerID = b.BorrowerID " +
                                       "SET br.ReturnDate = CURRENT_DATE " +
                                       "WHERE b.BorrowerID = ? AND br.BookID = ? AND br.ReturnDate IS NULL";
                prepare = connect.prepareStatement(updateBorrowSql);
                prepare.setString(1, getData.borrowerId);
                prepare.setInt(2, selectedBook.getBookId());
                int borrowUpdated = prepare.executeUpdate();

                // Update available copies in Book
                String updateBookSql = "UPDATE Book SET AvailableCopies = AvailableCopies + 1 " +
                                     "WHERE BookID = ?";
                prepare = connect.prepareStatement(updateBookSql);
                prepare.setInt(1, selectedBook.getBookId());
                int bookUpdated = prepare.executeUpdate();

                if (borrowUpdated > 0 && bookUpdated > 0) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book returned successfully!");
                    alert.showAndWait();

                    // Refresh the borrowed books list
                    showBorrowedBooks();
                    // Clear selection
                    returnBook_title.setText("");
                    returnBook_author.setText("");
                    returnBook_borrowDate.setText("");
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to return book");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error returning book: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void searchBooks() {
        String searchTerm = search_field.getText();
        ObservableList<availableBooks> searchResults = dataList(searchTerm);
        availableBooks_tableView.setItems(searchResults);
    }

    @FXML
    public void clearSearch() {
        search_field.clear();
        showAvailableBooks();
    }

    // Add key event handler for search field
    private void setupSearchField() {
        search_field.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                searchBooks();
            }
        });
    }

    @FXML
    public void showNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/notification.fxml"));
            Parent root = loader.load();
            
            Stage notificationStage = new Stage();
            Scene scene = new Scene(root);
            
            notificationStage.initStyle(StageStyle.UNDECORATED);
            notificationStage.setScene(scene);
            
            // Position the notification window near the notification button
            Button notifButton = notification_btn;
            javafx.geometry.Bounds bounds = notifButton.localToScreen(notifButton.getBoundsInLocal());
            notificationStage.setX(bounds.getMaxX() - 400); // 400 is the width of notification window
            notificationStage.setY(bounds.getMaxY() + 10);
            
            notificationStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class availableBooks {
        private int bookId;
        private String title;
        private String authors;
        private String genres;
        private int totalCopies;
        private int availableCopies;
        private int totalBorrows;

        public availableBooks(int bookId, String title, String authors, String genres, 
                            int totalCopies, int availableCopies, int totalBorrows) {
            this.bookId = bookId;
            this.title = title;
            this.authors = authors;
            this.genres = genres;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
            this.totalBorrows = totalBorrows;
        }

        public int getBookId() { return bookId; }
        public String getTitle() { return title; }
        public String getAuthors() { return authors; }
        public String getGenres() { return genres; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }
        public int getTotalBorrows() { return totalBorrows; }
    }

    public class WishlistBook {
        private int bookId;
        private String title;
        private String authors;
        private String genres;
        private String dateAdded;

        public WishlistBook(int bookId, String title, String authors, String genres, String dateAdded) {
            this.bookId = bookId;
            this.title = title;
            this.authors = authors;
            this.genres = genres;
            this.dateAdded = dateAdded;
        }

        public int getBookId() { return bookId; }
        public String getTitle() { return title; }
        public String getAuthors() { return authors; }
        public String getGenres() { return genres; }
        public String getDateAdded() { return dateAdded; }
    }

    public void addToWishlist() {
        availableBooks book = availableBooks_tableView.getSelectionModel().getSelectedItem();
        
        if (book == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to add to wishlist.");
            alert.showAndWait();
            return;
        }

        try {
            connect = Database.connectDB();
            
            // First get the AccountID for the current user
            String getAccountSql = "SELECT AccountID FROM UserAccount WHERE BorrowerID = ?";
            prepare = connect.prepareStatement(getAccountSql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            
            if (!result.next()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Could not find your account information.");
                alert.showAndWait();
                return;
            }
            
            int accountId = result.getInt("AccountID");

            // Check if book is already in wishlist
            String checkSql = "SELECT COUNT(*) as count FROM Wishlist WHERE AccountID = ? AND BookID = ?";
            prepare = connect.prepareStatement(checkSql);
            prepare.setInt(1, accountId);
            prepare.setInt(2, book.getBookId());
            result = prepare.executeQuery();
            
            if (result.next() && result.getInt("count") > 0) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("This book is already in your wishlist.");
                alert.showAndWait();
                return;
            }
            
            // Add to wishlist
            String addSql = "INSERT INTO Wishlist (AccountID, BookID) VALUES (?, ?)";
            prepare = connect.prepareStatement(addSql);
            prepare.setInt(1, accountId);
            prepare.setInt(2, book.getBookId());
            prepare.executeUpdate();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Book added to wishlist successfully!");
            alert.showAndWait();

            // Refresh wishlist if visible
            if (!availableBooks_form.isVisible()) {
                showWishlist();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error adding to wishlist: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void removeFromWishlist() {
        WishlistBook book = wishlist_tableView.getSelectionModel().getSelectedItem();
        
        if (book == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to remove from wishlist.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to remove this book from your wishlist?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                connect = Database.connectDB();
                
                // Get AccountID
                String getAccountSql = "SELECT AccountID FROM UserAccount WHERE BorrowerID = ?";
                prepare = connect.prepareStatement(getAccountSql);
                prepare.setString(1, getData.borrowerId);
                result = prepare.executeQuery();
                
                if (!result.next()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Could not find your account information.");
                    alert.showAndWait();
                    return;
                }
                
                int accountId = result.getInt("AccountID");

                // Remove from wishlist
                String removeSql = "DELETE FROM Wishlist WHERE AccountID = ? AND BookID = ?";
                prepare = connect.prepareStatement(removeSql);
                prepare.setInt(1, accountId);
                prepare.setInt(2, book.getBookId());
                prepare.executeUpdate();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Book removed from wishlist successfully!");
                alert.showAndWait();

                showWishlist();
                
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error removing from wishlist: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public ObservableList<WishlistBook> getWishlistBooks() {
        ObservableList<WishlistBook> wishlistBooks = FXCollections.observableArrayList();
        
        String sql = "SELECT b.BookID, b.Title, " +
                    "GROUP_CONCAT(DISTINCT a.FullName SEPARATOR ', ') as Authors, " +
                    "GROUP_CONCAT(DISTINCT g.Name SEPARATOR ', ') as Genres, " +
                    "w.DateAdded " +
                    "FROM Wishlist w " +
                    "JOIN Book b ON w.BookID = b.BookID " +
                    "JOIN UserAccount ua ON w.AccountID = ua.AccountID " +
                    "LEFT JOIN BookAuthor ba ON b.BookID = ba.BookID " +
                    "LEFT JOIN Author a ON ba.AuthorID = a.AuthorID " +
                    "LEFT JOIN BookGenre bg ON b.BookID = bg.BookID " +
                    "LEFT JOIN Genre g ON bg.GenreID = g.GenreID " +
                    "WHERE ua.BorrowerID = ? " +
                    "GROUP BY b.BookID, w.DateAdded";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            
            while (result.next()) {
                WishlistBook book = new WishlistBook(
                    result.getInt("BookID"),
                    result.getString("Title"),
                    result.getString("Authors"),
                    result.getString("Genres"),
                    result.getString("DateAdded")
                );
                wishlistBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return wishlistBooks;
    }

    public void showWishlist() {
        ObservableList<WishlistBook> wishlistBooks = getWishlistBooks();
        
        col_wl_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_wl_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        col_wl_genre.setCellValueFactory(new PropertyValueFactory<>("genres"));
        col_wl_dateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        
        wishlist_tableView.setItems(wishlistBooks);
    }

    private void loadUserInfo() {
        try {
            connect = Database.connectDB();
            
            String sql = "SELECT b.FullName, b.Phone, ua.Username " +
                        "FROM Borrower b " +
                        "JOIN UserAccount ua ON b.BorrowerID = ua.BorrowerID " +
                        "WHERE b.BorrowerID = ?";
            
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            
            if (result.next()) {
                settings_fullname.setText(result.getString("FullName"));
                settings_phone.setText(result.getString("Phone"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePersonalInfo() {
        String fullName = settings_fullname.getText().trim();
        String phone = settings_phone.getText().trim();
        
        if (fullName.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your full name");
            alert.showAndWait();
            return;
        }
        
        try {
            connect = Database.connectDB();
            
            String sql = "UPDATE Borrower SET FullName = ?, Phone = ? WHERE BorrowerID = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, fullName);
            prepare.setString(2, phone);
            prepare.setString(3, getData.borrowerId);
            
            prepare.executeUpdate();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Personal information updated successfully!");
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error updating personal information: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void changePassword() {
        String currentPassword = settings_currentPassword.getText();
        String newPassword = settings_newPassword.getText();
        String confirmPassword = settings_confirmPassword.getText();
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all password fields");
            alert.showAndWait();
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("New password and confirm password do not match");
            alert.showAndWait();
            return;
        }
        
        try {
            connect = Database.connectDB();
            
            // Verify current password
            String checkSql = "SELECT Password FROM UserAccount WHERE BorrowerID = ?";
            prepare = connect.prepareStatement(checkSql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            
            if (result.next()) {
                String storedPassword = result.getString("Password");
                if (!currentPassword.equals(storedPassword)) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Current password is incorrect");
                    alert.showAndWait();
                    return;
                }
            }
            
            // Update password
            String updateSql = "UPDATE UserAccount SET Password = ? WHERE BorrowerID = ?";
            prepare = connect.prepareStatement(updateSql);
            prepare.setString(1, newPassword);
            prepare.setString(2, getData.borrowerId);
            prepare.executeUpdate();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Password changed successfully!");
            alert.showAndWait();
            
            // Clear password fields
            settings_currentPassword.clear();
            settings_newPassword.clear();
            settings_confirmPassword.clear();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error changing password: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static class BorrowedBookDisplay {
        private String title;
        private String authors;
        private String borrowDate;
        private String returnDate;
        private String status;
        public BorrowedBookDisplay(String title, String authors, String borrowDate, String returnDate, String status) {
            this.title = title;
            this.authors = authors;
            this.borrowDate = borrowDate;
            this.returnDate = returnDate;
            this.status = status;
        }
        public String getTitle() { return title; }
        public String getAuthors() { return authors; }
        public String getBorrowDate() { return borrowDate; }
        public String getReturnDate() { return returnDate; }
        public String getStatus() { return status; }
    }

    public ObservableList<BorrowedBookDisplay> getAllBorrowedBooks() {
        ObservableList<BorrowedBookDisplay> list = FXCollections.observableArrayList();
        String sql = "SELECT b.Title, GROUP_CONCAT(a.FullName SEPARATOR ', ') as Authors, br.BorrowDate, br.ReturnDate " +
                "FROM Book b " +
                "JOIN BorrowEntry br ON b.BookID = br.BookID " +
                "LEFT JOIN BookAuthor ba ON b.BookID = ba.BookID " +
                "LEFT JOIN Author a ON ba.AuthorID = a.AuthorID " +
                "WHERE br.BorrowerID = ? " +
                "GROUP BY b.BookID, br.BorrowDate, br.ReturnDate";
        connect = Database.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, getData.borrowerId);
            result = prepare.executeQuery();
            while (result.next()) {
                String returnDate = result.getString("ReturnDate");
                String status = (returnDate == null || returnDate.isEmpty()) ? "Borrowing" : "Returned";
                list.add(new BorrowedBookDisplay(
                    result.getString("Title"),
                    result.getString("Authors"),
                    result.getString("BorrowDate"),
                    returnDate == null ? "" : returnDate,
                    status
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void showAllBorrowedBooks() {
        ObservableList<BorrowedBookDisplay> list = getAllBorrowedBooks();
        col_bb_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_bb_author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        col_bb_borrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        col_bb_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        col_bb_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        borrowedBooks_tableView.setItems(list);
    }
}
