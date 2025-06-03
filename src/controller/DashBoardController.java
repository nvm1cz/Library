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
    private Label studentNumber_label;

    @FXML
    private Label borrowId_label;

    @FXML
    private Button availableBooks_btn;

    @FXML
    private Button issueBooks_btn;

    @FXML
    private Button returnBooks_btn;

    @FXML
    private Button savedBooks_btn;

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
    private TableColumn<availableBooks, Integer> col_ab_totalCopies;

    @FXML
    private TableColumn<availableBooks, Integer> col_ab_availableCopies;

    @FXML
    private TableColumn<availableBooks, Integer> col_ab_totalBorrows;

    @FXML
    private ImageView availableBooks_imageView;

    @FXML
    private Button save_btn;

    @FXML
    private Label availableBooks_title;

    @FXML
    private Button take_btn;

    @FXML
    private AnchorPane mainCenter_form;

    @FXML
    private Button halfNav_availableBtn;

    @FXML
    private AnchorPane halfNav_form;

    @FXML
    private Button halfNav_returnBtn;

    @FXML
    private Button halfNav_saveBtn;

    @FXML
    private Button halfNav_takeBtn;

    @FXML
    private Circle smallCircle_image;

    @FXML
    private AnchorPane issue_form;

    @FXML
    private AnchorPane returnBook_form;

    @FXML
    private AnchorPane savedBook_form;

    @FXML
    private Label currentForm_label;

     @FXML
    private TextField take_BookTitle;

    @FXML
    private TextField take_FirstName;

    @FXML
    private TextField take_LastName;

    @FXML
    private Label take_StudentNumber;

    @FXML
    private Label take_authorLabel;

    @FXML
    private Button take_clearBtn;

    @FXML
    private Label take_dateLabel;

    @FXML
    private Label take_genreLabel;

    @FXML
    private ImageView take_imageView;

    @FXML
    private Label take_issuedDate;

    @FXML
    private Button take_takeBtn;

    @FXML
    private Label take_titleLabel;

    private Image image;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public void studentNumberLabel() {
        take_StudentNumber.setText(getData.studentCode);
    }

     public void clearTakeData() {

        take_BookTitle.setText("");
        take_titleLabel.setText("");
        take_authorLabel.setText("");
        take_genreLabel.setText("");
        take_dateLabel.setText("");
        take_imageView.setImage(null);

    }

    public void displayDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new java.util.Date());
        take_issuedDate.setText(date);
    }
    //TO SHOW THE BOOKS DATA

    public ObservableList<availableBooks> dataList() {
        ObservableList<availableBooks> listBooks = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Book WHERE AvailableCopies > 0";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                availableBooks book = new availableBooks(
                    result.getInt("BookID"),
                    result.getString("Title"),
                    result.getString("Author"),
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
        col_ab_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_ab_totalCopies.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        col_ab_availableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        col_ab_totalBorrows.setCellValueFactory(new PropertyValueFactory<>("totalBorrows"));
        
        availableBooks_tableView.setItems(listBooks);
    }

    public void selectAvailableBooks() {
        availableBooks bookData = availableBooks_tableView.getSelectionModel().getSelectedItem();
        int num = availableBooks_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        availableBooks_title.setText(bookData.getTitle());
    }

    public void takeBook() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String updateBookSql = "UPDATE Book SET AvailableCopies = AvailableCopies - 1, TotalBorrows = TotalBorrows + 1 WHERE BookID = ? AND AvailableCopies > 0";

        connect = Database.connectDB();

        try {
            Alert alert;

            if (take_FirstName.getText().isEmpty()
                    || take_LastName.getText().isEmpty()) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please type complete Student Details");
                alert.showAndWait();
            } else if (take_titleLabel.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please indicate the book you want to take.");
                alert.showAndWait();
            } else {
                // Check if book is available
                String checkSql = "SELECT BookID, AvailableCopies FROM Book WHERE Title = ?";
                prepare = connect.prepareStatement(checkSql);
                prepare.setString(1, take_titleLabel.getText());
                result = prepare.executeQuery();

                if (result.next() && result.getInt("AvailableCopies") > 0) {
                    int bookId = result.getInt("BookID");

                    // Update book available copies
                    prepare = connect.prepareStatement(updateBookSql);
                    prepare.setInt(1, bookId);
                    int updated = prepare.executeUpdate();

                    if (updated > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Success Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully borrowed the book!");
                        alert.showAndWait();

                        clearTakeData();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Book is no longer available for borrowing.");
                        alert.showAndWait();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book is not available for borrowing.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while processing your request: " + e.getMessage());
            alert.showAndWait();
        }
    }
    

    //SHOWING BOOKS DATA
    private ObservableList<availableBooks> listBook;

    public void abTakeButton(ActionEvent event) {
        if (event.getSource() == take_btn) {
            availableBooks book = availableBooks_tableView.getSelectionModel().getSelectedItem();
            
            if (book == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to borrow.");
                alert.showAndWait();
                return;
            }

            // Confirm borrow
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Borrow");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Do you want to borrow the book: " + book.getTitle() + "?");
            
            if (confirmAlert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                // Check if book is still available
                if (book.getAvailableCopies() > 0) {
                    try {
                        connect = Database.connectDB();
                        
                        // First check if user exists in Borrower table
                        String checkUserSql = "SELECT BorrowerID FROM Borrower WHERE StudentCode = ?";
                        prepare = connect.prepareStatement(checkUserSql);
                        prepare.setString(1, getData.studentCode);
                        result = prepare.executeQuery();
                        
                        String borrowerId;
                        if (!result.next()) {
                            // Create new borrower if not exists
                            borrowerId = "SV" + getData.studentCode;
                            String insertBorrowerSql = "INSERT INTO Borrower (BorrowerID, FullName, StudentCode, IsStudent, Phone) VALUES (?, ?, ?, TRUE, ?)";
                            prepare = connect.prepareStatement(insertBorrowerSql);
                            prepare.setString(1, borrowerId);
                            prepare.setString(2, getData.studentCode); // Using student code as name temporarily
                            prepare.setString(3, getData.studentCode);
                            prepare.setString(4, ""); // Empty phone
                            prepare.executeUpdate();
                        }

                        // Update book available copies
                        String updateBookSql = "UPDATE Book SET AvailableCopies = AvailableCopies - 1, TotalBorrows = TotalBorrows + 1 WHERE BookID = ? AND AvailableCopies > 0";
                        prepare = connect.prepareStatement(updateBookSql);
                        prepare.setInt(1, book.getBookId());
                        int updated = prepare.executeUpdate();

                        if (updated > 0) {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Success Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully borrowed the book!");
                            alert.showAndWait();

                            // Refresh the table
                            showAvailableBooks();
                        } else {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Book is no longer available for borrowing.");
                            alert.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Error processing book borrow: " + e.getMessage());
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book is no longer available for borrowing.");
                    alert.showAndWait();
                }
            }
        }
    }

    public void studentNumber() {
        studentNumber_label.setText(getData.studentCode);
        
        // Get borrowId for current user from borrower table
        String sql = "SELECT BorrowerID as latestBorrow FROM Borrower WHERE StudentCode = ?";
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, getData.studentCode);
            result = prepare.executeQuery();
            
            if(result.next()) {
                String borrowId = result.getString("latestBorrow");
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

            issue_form.setVisible(false);
            availableBooks_form.setVisible(true);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Available Books");

        } else if (event.getSource() == halfNav_takeBtn) {

            issue_form.setVisible(true);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Issue Books");

        } else if (event.getSource() == halfNav_returnBtn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(true);

            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Return Books");

            

        } else if (event.getSource() == halfNav_saveBtn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(true);
            returnBook_form.setVisible(false);

            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Saved Books");

        }

    }

    public void navButtonDesign(ActionEvent event) {
        if (event.getSource() == availableBooks_btn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(true);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

             halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");


            currentForm_label.setText("Available Books");

        } else if (event.getSource() == issueBooks_btn) {

            issue_form.setVisible(true);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

             currentForm_label.setText("Issue Books");

        } else if (event.getSource() == returnBooks_btn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(true);

            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");


            currentForm_label.setText("Return Books");



        } else if (event.getSource() == savedBooks_btn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(true);
            returnBook_form.setVisible(false);

            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

             currentForm_label.setText("Saved Books");
        }
        
    }
    
    private double x = 0;
    private double y = 0;

    public void sliderArrow(){

        TranslateTransition slide = new TranslateTransition();

        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(nav_form);
        slide.setToX(-226);

        TranslateTransition slide1 = new TranslateTransition();

        slide1.setDuration(Duration.seconds(0.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(-226+90);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(.5));
        slide2.setNode(halfNav_form);
        slide2.setToX(0);

     
        slide.setOnFinished((ActionEvent event) -> {
            arrow_btn.setVisible(false);
            bars_btn.setVisible(true);
        });
        slide2.play();
        slide1.play();
        slide.play();
    }

    public void sliderBars(){

        TranslateTransition slide = new TranslateTransition();

        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(nav_form);
        slide.setToX(0);

        TranslateTransition slide1 = new TranslateTransition();
        slide1.setDuration(Duration.seconds(0.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(0);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(.5));
        slide2.setNode(halfNav_form);
        slide2.setToX(-77);

        slide.setOnFinished((ActionEvent event) -> {
            arrow_btn.setVisible(true);
            bars_btn.setVisible(false);
        });
        slide2.play();
        slide1.play();
        slide.play();
    }
    //qua bo doiiii

    @FXML
    public void logout(ActionEvent event) {
        try {
            if (event.getSource() == logout_btn) {
                // TO SWAP FROM DASHBOARD TO LOGIN FORM
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

                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();

                logout_btn.getScene().getWindow().hide();

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

    // @override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        //        TO SHOW THE AVAILABLE BOOKS
        showAvailableBooks();

        studentNumber();

        studentNumberLabel();

        displayDate();
    }
}
