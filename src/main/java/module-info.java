module lk.wms.aquaflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires javafx.media;
    requires java.sql;
    requires static lombok;
    requires mysql.connector.j;
    requires net.sf.jasperreports.core;


    opens lk.wms.aquaflow to javafx.fxml;
    opens lk.wms.aquaflow.controller to javafx.fxml;
    opens lk.wms.aquaflow.controller.modal to javafx.fxml;
    exports lk.wms.aquaflow;
    exports lk.wms.aquaflow.controller;
    exports lk.wms.aquaflow.entity;
    exports lk.wms.aquaflow.dao;
    exports lk.wms.aquaflow.dao.custom;
    exports lk.wms.aquaflow.bo;
    exports lk.wms.aquaflow.bo.custom;
    exports lk.wms.aquaflow.bo.custom.impl;
    exports lk.wms.aquaflow.dto;
    exports lk.wms.aquaflow.util;
    exports lk.wms.aquaflow.view.tm;
    exports lk.wms.aquaflow.controller.modal;

}