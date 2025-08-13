module lk.wms.aquaflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires javafx.media;


    opens lk.wms.aquaflow to javafx.fxml;
    exports lk.wms.aquaflow;
    exports lk.wms.aquaflow.controller;
    exports lk.wms.aquaflow.controller.modal;

}