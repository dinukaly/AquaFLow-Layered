module lk.wms.aquaflow {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.wms.aquaflow to javafx.fxml;
    exports lk.wms.aquaflow;
}