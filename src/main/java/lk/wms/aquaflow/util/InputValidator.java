package lk.wms.aquaflow.util;

import javafx.scene.control.*;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String ERROR_BORDER_STYLE = "-fx-border-color: red;";
    private static final String DEFAULT_BORDER_STYLE = "";

    public static boolean isNotEmpty(TextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            AlertUtil.showError(fieldName + " cannot be empty!");
            return false;
        }
        return true;
    }

    public static boolean isSelected(ComboBox<?> comboBox, String fieldName) {
        if (comboBox.getValue() == null) {
            AlertUtil.showError("Please select a " + fieldName + "!");
            return false;
        }
        return true;
    }
    public static boolean isDateSelected(DatePicker datePicker, String fieldName) {
        if (datePicker.getValue() == null) {
            AlertUtil.showError("Please select a " + fieldName + "!");
            return false;
        }
        return true;
    }


    public static boolean matchesPattern(TextField field, String pattern, String fieldName, String errorMessage) {
        if (!Pattern.matches(pattern, field.getText().trim())) {
            markInvalid(field);
            AlertUtil.showError(errorMessage != null ? errorMessage : fieldName + " format is invalid!");
            return false;
        }
        return true;
    }

    public static boolean matchesPattern(TextField field, String pattern, String fieldName) {
        return matchesPattern(field, pattern, fieldName, null);
    }


    public static boolean isValidEmail(TextField field) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return matchesPattern(field, emailRegex, "Email", "Please enter a valid email address");
    }

    public static boolean isValidPhone(TextField field) {
        // Supports formats: 0771234567, 077-1234567, +94771234567, +94-77-1234567
        String phoneRegex = "^(\\+94|0)[1-9][0-9]{8}$|^(\\+94|0)-[1-9][0-9]-[0-9]{6}$";
        return matchesPattern(field, phoneRegex, "Phone number", "Please enter a valid phone number");
    }


    public static boolean isNumeric(TextField field, String fieldName) {
        String numericRegex = "^[0-9]+$";
        return matchesPattern(field, numericRegex, fieldName, fieldName + " must contain only numbers");
    }

    public static boolean isAlphabetic(TextField field, String fieldName) {
        String alphabeticRegex = "^[a-zA-Z\\s]+$";
        return matchesPattern(field, alphabeticRegex, fieldName, fieldName + " must contain only letters");
    }

    public static boolean isAlphanumeric(TextField field, String fieldName) {
        String alphanumericRegex = "^[a-zA-Z0-9\\s]+$";
        return matchesPattern(field, alphanumericRegex, fieldName, fieldName + " must contain only letters and numbers");
    }

    public static boolean isNotEmpty(TextArea area, String fieldName) {
        if (area.getText().trim().isEmpty()) {
            AlertUtil.showError(fieldName + " cannot be empty!");
            markInvalid(area);
            return false;
        }
        return true;
    }

    // Style utilities
    public static void markInvalid(Control control) {
        control.setStyle(ERROR_BORDER_STYLE);
    }

    public static void markInvalid(TextArea area) {
        area.setStyle("-fx-border-color: #ff0000; -fx-border-width: 1px;");
    }

    public static void clearStyle(Control... controls) {
        for (Control control : controls) {
            control.setStyle(DEFAULT_BORDER_STYLE);
        }
    }

    public static void clearStyle(TextArea... areas) {
        for (TextArea area : areas) {
            area.setStyle("");
        }
    }

}
