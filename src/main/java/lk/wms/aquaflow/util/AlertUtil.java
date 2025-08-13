package lk.wms.aquaflow.util;

import javafx.scene.control.Alert;

public class AlertUtil {
    public static void showInfo(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Info", message);
    }

    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Error", message);
    }

    public static void showSuccess(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Success", message);
    }

    public static void showFailure(String message) {
        showAlert(Alert.AlertType.ERROR, "Failed", message);
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
