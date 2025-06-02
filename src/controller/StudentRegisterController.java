package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import dao.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StudentRegisterController implements Initializable {

    @FXML
    private TextField studentNumber;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Button registerBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    public void register() {
        String sql = "INSERT INTO students (studentNumber, firstName, lastName, password) VALUES (?, ?, ?, ?)";
        String checkStudent = "SELECT * FROM students WHERE studentNumber = ?";
        
        connect = Database.connectDB();
        
        try {
            Alert alert;
            
            if (studentNumber.getText().isEmpty() || 
                firstName.getText().isEmpty() ||
                lastName.getText().isEmpty() ||
                password.getText().isEmpty() || 
                confirmPassword.getText().isEmpty()) {
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
                
            } else if (!password.getText().equals(confirmPassword.getText())) {
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Password does not match");
                alert.showAndWait();
                
            } else {
                // Check if student number already exists
                prepare = connect.prepareStatement(checkStudent);
                prepare.setString(1, studentNumber.getText());
                result = prepare.executeQuery();
                
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Student #" + studentNumber.getText() + " already exists!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, studentNumber.getText());
                    prepare.setString(2, firstName.getText());
                    prepare.setString(3, lastName.getText());
                    prepare.setString(4, password.getText());
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered!");
                    alert.showAndWait();
                    
                    // Clear fields
                    studentNumber.setText("");
                    firstName.setText("");
                    lastName.setText("");
                    password.setText("");
                    confirmPassword.setText("");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/document.fxml"));
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
            
            // Hide the registration form
            registerBtn.getScene().getWindow().hide();
            
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
    }
} 