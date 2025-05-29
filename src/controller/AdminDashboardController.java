package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import dao.Database;
import model.students;
import model.issuedBooks;
import model.availableBooks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.sql.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AdminDashboardController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button manageBooks_btn;

    @FXML
    private Button manageStudents_btn;

    @FXML
    private Button issuedBooks_btn;

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
    private TableColumn<availableBooks, String> col_bookType;

    @FXML
    private TableColumn<availableBooks, String> col_publishedDate;

    @FXML
    private AnchorPane manageStudents_form;

    @FXML
    private TableView<students> students_tableView;

    @FXML
    private TableColumn<students, String> col_studentNumber;

    @FXML
    private TableColumn<students, String> col_password;

    @FXML
    private AnchorPane issuedBooks_form;

    @FXML
    private TableView<issuedBooks> issuedBooks_tableView;

    @FXML
    private TableColumn<issuedBooks, String> col_issueStudentNumber;

    @FXML
    private TableColumn<issuedBooks, String> col_issueFirstName;

    @FXML
    private TableColumn<issuedBooks, String> col_issueLastName;

    @FXML
    private TableColumn<issuedBooks, String> col_issueBookTitle;

    @FXML
    private TableColumn<issuedBooks, String> col_issueDate;

    @FXML
    private TableColumn<issuedBooks, String> col_issueStatus;

    @FXML
    private TextField book_title;

    @FXML
    private TextField book_author;

    @FXML
    private TextField book_type;

    @FXML
    private DatePicker book_date;

    @FXML
    private Button addBook_btn;

    @FXML
    private Button deleteBook_btn;

    @FXML
    private Button clearBook_btn;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    public void switchForm(ActionEvent event) {
        if (event.getSource() == manageBooks_btn) {
            manageBooks_form.setVisible(true);
            manageStudents_form.setVisible(false);
            issuedBooks_form.setVisible(false);
            
            showBooks();
        } else if (event.getSource() == manageStudents_btn) {
            manageBooks_form.setVisible(false);
            manageStudents_form.setVisible(true);
            issuedBooks_form.setVisible(false);
            
            showStudents();
        } else if (event.getSource() == issuedBooks_btn) {
            manageBooks_form.setVisible(false);
            manageStudents_form.setVisible(false);
            issuedBooks_form.setVisible(true);
            
            showIssuedBooks();
        }
    }

    public ObservableList<availableBooks> booksList() {
        ObservableList<availableBooks> bookList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM book";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            availableBooks books;
            
            while (result.next()) {
                books = new availableBooks(
                        result.getString("bookTitle"),
                        result.getString("author"),
                        result.getString("bookType"),
                        result.getString("date")
                );
                bookList.add(books);
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
        col_bookType.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_publishedDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        books_tableView.setItems(listBooks);
    }

    public ObservableList<students> studentsList() {
        ObservableList<students> studentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            students student;
            
            while (result.next()) {
                student = new students(
                        result.getString("studentNumber"),
                        result.getString("password")
                );
                studentList.add(student);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return studentList;
    }

    public void showStudents() {
        ObservableList<students> listStudents = studentsList();
        
        col_studentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        
        students_tableView.setItems(listStudents);
    }

    public ObservableList<issuedBooks> issuedBooksList() {
        ObservableList<issuedBooks> issuedList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM take";
        
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            issuedBooks issuedB;
            
            while (result.next()) {
                issuedB = new issuedBooks(
                        result.getString("studentNumber"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("bookTitle"),
                        result.getString("date"),
                        result.getString("checkReturn")
                );
                issuedList.add(issuedB);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return issuedList;
    }

    public void showIssuedBooks() {
        ObservableList<issuedBooks> listIssued = issuedBooksList();
        
        col_issueStudentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        col_issueFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_issueLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_issueBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_issueDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_issueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        issuedBooks_tableView.setItems(listIssued);
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
        String sql = "INSERT INTO book (bookTitle, author, bookType, date) VALUES (?, ?, ?, ?)";
        
        connect = Database.connectDB();
        
        try {
            // Check if fields are empty
            if (book_title.getText().isEmpty() || 
                book_author.getText().isEmpty() || 
                book_type.getText().isEmpty() || 
                book_date.getValue() == null) {
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields.");
                alert.showAndWait();
                return;
            }
            
            // Check if book already exists
            String checkSql = "SELECT * FROM book WHERE bookTitle = ?";
            prepare = connect.prepareStatement(checkSql);
            prepare.setString(1, book_title.getText());
            result = prepare.executeQuery();
            
            if (result.next()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Book already exists!");
                alert.showAndWait();
                return;
            }
            
            // Add new book
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, book_title.getText());
            prepare.setString(2, book_author.getText());
            prepare.setString(3, book_type.getText());
            prepare.setDate(4, Date.valueOf(book_date.getValue()));
            
            prepare.executeUpdate();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Book added successfully!");
            alert.showAndWait();
            
            // Clear fields and refresh table
            clearBookFields();
            showBooks();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBook() {
        String sql = "DELETE FROM book WHERE bookTitle = ?";
        
        connect = Database.connectDB();
        
        try {
            // Get selected book
            availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
            
            if (selectedBook == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to delete.");
                alert.showAndWait();
                return;
            }
            
            // Confirm deletion
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to delete this book?");
            
            if (confirmAlert.showAndWait().get().getButtonData().isDefaultButton()) {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, selectedBook.getTitle());
                prepare.executeUpdate();
                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book deleted successfully!");
                alert.showAndWait();
                
                // Clear fields and refresh table
                clearBookFields();
                showBooks();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearBookFields() {
        book_title.setText("");
        book_author.setText("");
        book_type.setText("");
        book_date.setValue(null);
        books_tableView.getSelectionModel().clearSelection();
    }

    // Add event handler for table selection
    public void selectBook() {
        availableBooks selectedBook = books_tableView.getSelectionModel().getSelectedItem();
        
        if (selectedBook != null) {
            book_title.setText(selectedBook.getTitle());
            book_author.setText(selectedBook.getAuthor());
            book_type.setText(selectedBook.getGenre());
            book_date.setValue(LocalDate.parse(selectedBook.getDate()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBooks();
        
        // Add listener for table selection
        books_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectBook();
            }
        });
    }
} 