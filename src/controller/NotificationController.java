package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import dao.DBConnect;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import java.time.LocalDate;

public class NotificationController implements Initializable {
    
    @FXML
    private ListView<HBox> notificationList;
    
    @FXML
    private Button close_btn;

    @FXML
    private AnchorPane ratingDialog;

    @FXML
    private Label bookTitle_label;

    @FXML
    private HBox starsContainer;

    @FXML
    private TextArea comment_area;

    @FXML
    private Button submit_rating_btn;

    @FXML
    private Button cancel_rating_btn;

    @FXML
    private AnchorPane commentDialog;

    @FXML
    private Label commentBookTitle_label;

    @FXML
    private TextArea viewComment_area;

    @FXML
    private TextArea addComment_area;

    @FXML
    private Button submitComment_btn;

    @FXML
    private Button cancelComment_btn;
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private int currentEntryId;
    private int selectedRating = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAllNotifications();
        setupStars();
    }

    private void setupStars() {
        starsContainer.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            FontAwesomeIcon star = new FontAwesomeIcon();
            star.setGlyphName("STAR");
            star.setSize("2em");
            star.setFill(Color.GRAY);
            final int rating = i;
            
            star.setOnMouseEntered(e -> highlightStars(rating));
            star.setOnMouseExited(e -> {
                if (selectedRating == 0) {
                    resetStars();
                } else {
                    highlightStars(selectedRating);
                }
            });
            star.setOnMouseClicked(e -> {
                selectedRating = rating;
                highlightStars(rating);
            });
            
            starsContainer.getChildren().add(star);
        }
    }
    
    private void loadAllNotifications() {
        ObservableList<HBox> allNotis = FXCollections.observableArrayList();
        String sql = "SELECT Message, DateCreated, IsRead FROM Notification WHERE AccountID = ? ORDER BY DateCreated DESC";
        connect = DBConnect.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, model.getData.accountId);
            result = prepare.executeQuery();
            while (result.next()) {
                String message = result.getString("Message");
                String date = result.getString("DateCreated");
                HBox box = new HBox(10);
                box.setAlignment(Pos.CENTER_LEFT);
                Label msgLabel = new Label(message + " (" + date + ")");
                msgLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #222; -fx-font-weight: bold; -fx-opacity: 1;");
                box.getChildren().add(msgLabel);
                allNotis.add(box);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql2 = "SELECT be.EntryID, b.Title, be.ReturnDate, (SELECT COUNT(*) FROM Review r WHERE r.EntryID = be.EntryID) > 0 as IsRated FROM BorrowEntry be JOIN Book b ON be.BookID = b.BookID WHERE be.BorrowerID = ? AND be.ReturnDate IS NOT NULL ORDER BY be.ReturnDate DESC";
        try {
            prepare = connect.prepareStatement(sql2);
            prepare.setString(1, model.getData.borrowerId);
            result = prepare.executeQuery();
            while(result.next()) {
                HBox bookBox = createBookBox(
                    result.getInt("EntryID"),
                    result.getString("Title"),
                    result.getDate("ReturnDate").toLocalDate(),
                    result.getBoolean("IsRated")
                );
                allNotis.add(bookBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationList.setItems(allNotis);
        notificationList.getSelectionModel().clearSelection();
        notificationList.setOnMouseClicked(e -> notificationList.getSelectionModel().clearSelection());
    }
    
    private HBox createBookBox(int entryId, String bookTitle, LocalDate returnDate, boolean isRated) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(350);
        box.getStyleClass().add("notification-item");
        
        VBox messageBox = new VBox(5);
        messageBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(messageBox, Priority.ALWAYS);
        
        Label titleLabel = new Label(bookTitle);
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-weight: bold;");
        
        Label returnLabel = new Label("Returned on: " + returnDate.toString());
        returnLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        messageBox.getChildren().addAll(titleLabel, returnLabel);
        
        if (!isRated) {
            Button rateButton = new Button("Rate Book");
            rateButton.getStyleClass().add("rate-btn");
            rateButton.setOnAction(e -> showRatingDialog(entryId, bookTitle));
            messageBox.getChildren().add(rateButton);
        } else {
            HBox actionBox = new HBox(5);
            
            Label ratedLabel = new Label("✓ Rated");
            ratedLabel.setTextFill(Color.GREEN);
            
            Button commentButton = new Button("View/Add Comment");
            commentButton.getStyleClass().add("comment-btn");
            commentButton.setOnAction(e -> showCommentDialog(entryId, bookTitle));
            
            actionBox.getChildren().addAll(ratedLabel, commentButton);
            messageBox.getChildren().add(actionBox);
        }
        
        box.getChildren().add(messageBox);
        return box;
    }
    
    private void showRatingDialog(int entryId, String bookTitle) {
        currentEntryId = entryId;
        selectedRating = 0;
        
        bookTitle_label.setText(bookTitle);
        comment_area.clear();
        resetStars();
        
        ratingDialog.setVisible(true);
    }
    
    private void highlightStars(int rating) {
        for (int i = 0; i < starsContainer.getChildren().size(); i++) {
            FontAwesomeIcon star = (FontAwesomeIcon) starsContainer.getChildren().get(i);
            star.setFill(i < rating ? Color.GOLD : Color.GRAY);
        }
    }
    
    private void resetStars() {
        starsContainer.getChildren().forEach(node -> 
            ((FontAwesomeIcon) node).setFill(Color.GRAY)
        );
    }
    
    @FXML
    private void submitRating() {
        if (selectedRating == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a rating");
            alert.showAndWait();
            return;
        }

        String sql = "INSERT INTO Review (EntryID, Rating, Comment) VALUES (?, ?, ?)";
        
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, currentEntryId);
            prepare.setInt(2, selectedRating);
            prepare.setString(3, comment_area.getText());
            prepare.executeUpdate();
            
            ratingDialog.setVisible(false);
            loadAllNotifications();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error submitting review: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void cancelRating() {
        ratingDialog.setVisible(false);
        selectedRating = 0;
        resetStars();
    }
    
    @FXML
    private void closeNotifications() {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }

    private void showCommentDialog(int entryId, String bookTitle) {
        currentEntryId = entryId;
        
        commentBookTitle_label.setText(bookTitle);
        viewComment_area.clear();
        addComment_area.clear();
        
        // Load existing review
        String sql = "SELECT Rating, Comment FROM Review WHERE EntryID = ?";
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, entryId);
            result = prepare.executeQuery();
            
            if (result.next()) {
                int rating = result.getInt("Rating");
                String comment = result.getString("Comment");
                
                StringBuilder reviewText = new StringBuilder();
                reviewText.append("Rating: ");
                for (int i = 0; i < rating; i++) {
                    reviewText.append("★");
                }
                for (int i = rating; i < 5; i++) {
                    reviewText.append("☆");
                }
                reviewText.append("\n\nYour Review:\n").append(comment != null ? comment : "No comment added.");
                
                viewComment_area.setText(reviewText.toString());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        commentDialog.setVisible(true);
    }
    
    @FXML
    private void submitComment() {
        if (addComment_area.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please write a comment before submitting");
            alert.showAndWait();
            return;
        }

        String sql = "UPDATE Review SET Comment = ? WHERE EntryID = ?";
        
        connect = DBConnect.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, addComment_area.getText().trim());
            prepare.setInt(2, currentEntryId);
            prepare.executeUpdate();
            
            commentDialog.setVisible(false);
            loadAllNotifications();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Comment updated successfully!");
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error updating comment: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void cancelComment() {
        commentDialog.setVisible(false);
        addComment_area.clear();
    }
} 