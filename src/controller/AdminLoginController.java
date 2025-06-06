package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import dao.DBConnect;
import model.getData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminLoginController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

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

    public void login() {
        String sql = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
        
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();
            
            Alert alert;
            
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields.");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    getData.adminId = result.getInt("AdminID");
                    getData.adminName = result.getString("FullName");
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully logged in as admin!");
                    alert.showAndWait();
                    
                    // Hide login form
                    loginBtn.getScene().getWindow().hide();
                    
                    // Load admin dashboard
                    Parent root = FXMLLoader.load(getClass().getResource("/view/adminDashboard.fxml"));
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
                    
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username or Password.");
                    alert.showAndWait();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToStudentLogin() {
        try {
            loginBtn.getScene().getWindow().hide();
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void minimize() {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    public void exit() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
} 