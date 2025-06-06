package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.Statement;

import dao.DBConnect;
import model.getData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserLoginController implements Initializable {

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private double x = 0;
    private double y = 0;
    
    


    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login_Btn;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button adminLoginBtn;
    
    
    public void login(){
        String sql = "SELECT b.BorrowerID, b.FullName, u.Username, u.Password " +
                    "FROM Borrower b " +
                    "JOIN UserAccount u ON b.BorrowerID = u.BorrowerID " +
                    "WHERE u.Username = ? AND u.Password = ?";
        
        connect = DBConnect.connectDB();
        
        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();
            
            Alert alert;
            
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields.");
                alert.showAndWait();
            } else {
                if(result.next()){
                    getData.borrowerId = result.getString("BorrowerID");
                    getData.borrowerName = result.getString("FullName");
                    // Lấy AccountID
                    String getAccountIdSql = "SELECT AccountID FROM UserAccount WHERE BorrowerID = ?";
                    PreparedStatement ps = connect.prepareStatement(getAccountIdSql);
                    ps.setString(1, getData.borrowerId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        getData.accountId = rs.getInt("AccountID");
                    }
                    rs.close();
                    ps.close();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();
                    
                    username.getScene().getWindow().hide();
                    
                    Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    
                    root.setOnMousePressed((MouseEvent event) ->{
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });
                    
                    root.setOnMouseDragged((MouseEvent event) ->{
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
                    alert.setContentText("Wrong Username/Password!");
                    alert.showAndWait();
                }
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToAdminLogin() {
        try {
            login_Btn.getScene().getWindow().hide();
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

    @FXML
    public void minimize(){
        Stage stage = (Stage)minimize.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    public void exit(){
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
