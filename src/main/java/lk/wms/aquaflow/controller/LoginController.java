package lk.wms.aquaflow.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.LoginBO;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;



public class LoginController implements Initializable {
    public HBox rootNode;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogin;
    public RadioButton radioAdmin;
    public RadioButton radioOperator;
    public MediaView mediaView;

    private LoginBO loginBO = (LoginBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LOGIN);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showVideo();
        ToggleGroup toggleGroup = new ToggleGroup();
        radioAdmin.setToggleGroup(toggleGroup);
        radioOperator.setToggleGroup(toggleGroup);
        radioAdmin.setSelected(true);
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String userName = txtUserName.getText().trim();
        String password = txtPassword.getText();

        // Validate input fields
        if (userName.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter both username and password.");
            return;
        }

        // Determine user type
        String userType = radioAdmin.isSelected() ? "ADMIN" : "OPERATOR";

        try {

            boolean isAuthenticated = loginBO.validateUser(userName, password, userType);
            System.out.println("validateUser returned: " + isAuthenticated);

            if (isAuthenticated) {
                // Navigate to appropriate dashboard based on user type
                if ("ADMIN".equals(userType)) {
                    navigateToDashboard("/lk/wms/aquaflow/view/adminDashboard-view.fxml");
                } else {
                    navigateToDashboard("/lk/wms/aquaflow/view/operator-adminDashboard-view.fxml");
                }
            } else {
                showAlert("Login Failed", "Invalid credentials or unauthorized user type.");
            }

        } catch (Exception e) {
            showAlert("Database Error", "Failed to authenticate user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToDashboard(String fxmlPath) {
        try {
            Stage stage = (Stage) rootNode.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath))));
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert("Error", "Failed to load dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showVideo() {
        try {
            String videoPath = Objects.requireNonNull(getClass().getResource("/lk/wms/aquaflow/assets/Generated File June 06, 2025 - 10_04PM.mp4")).toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);
            mediaPlayer.play();
            mediaView.setMediaPlayer(mediaPlayer);
        } catch (Exception e) {
            System.out.println("Error loading video: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void forgotPasswordOnAction(ActionEvent event) {

    }

    public void radioOperatorOnAction(ActionEvent actionEvent) {
    }

    public void radioAdminOnAction(ActionEvent actionEvent) {
    }
}