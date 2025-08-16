package lk.wms.aquaflow.util;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.function.Consumer;

public class TableActionCell<T> {


    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> create(
            Consumer<T> onEdit,
            Consumer<T> onDelete
    ) {
        return new Callback<>() {
            @Override
            public TableCell<T, String> call(final TableColumn<T, String> param) {
                return new TableCell<>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView editIcon = new ImageView(
                                    new Image(getClass().getResourceAsStream("/lk/wms/aquaflow/assets/icons8-update-50.png"))
                            );
                            ImageView deleteIcon = new ImageView(
                                    new Image(getClass().getResourceAsStream("/lk/wms/aquaflow/assets/icons8-delete-16.png"))
                            );

                            editIcon.setFitWidth(20);
                            editIcon.setFitHeight(20);
                            deleteIcon.setFitWidth(20);
                            deleteIcon.setFitHeight(20);

                            editIcon.setOnMouseClicked(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onEdit.accept(rowItem);
                            });

                            deleteIcon.setOnMouseClicked(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onDelete.accept(rowItem);
                            });

                            HBox hbox = new HBox(10, editIcon, deleteIcon);
                            hbox.setPadding(new Insets(5));
                            setGraphic(hbox);
                        }
                        setText(null);
                    }
                };
            }
        };
    }
    
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> createWithCompleteAndDelete(
            Consumer<T> onComplete,
            Consumer<T> onDelete
    ) {
        return new Callback<>() {
            @Override
            public TableCell<T, String> call(final TableColumn<T, String> param) {
                return new TableCell<>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Create a Complete button with text
                            Button completeButton = new Button("Complete");
                            completeButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 8;");
                            
                            ImageView deleteIcon = new ImageView(
                                    new Image(getClass().getResourceAsStream("/lk/wms/aquaflow/assets/icons8-delete-16.png"))
                            );

                            deleteIcon.setFitWidth(20);
                            deleteIcon.setFitHeight(20);

                            completeButton.setOnAction(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onComplete.accept(rowItem);
                            });

                            deleteIcon.setOnMouseClicked(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onDelete.accept(rowItem);
                            });

                            HBox hbox = new HBox(10, completeButton, deleteIcon);
                            hbox.setPadding(new Insets(3));
                            setGraphic(hbox);
                        }
                        setText(null);
                    }
                };
            }
        };
    }
    
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> createWithEditCompleteDelete(
            Consumer<T> onEdit,
            Consumer<T> onComplete,
            Consumer<T> onDelete
    ) {
        return new Callback<>() {
            @Override
            public TableCell<T, String> call(final TableColumn<T, String> param) {
                return new TableCell<>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView editIcon = new ImageView(
                                    new Image(getClass().getResourceAsStream("/lk/wms/aquaflow/assets/icons8-update-50.png"))
                            );
                            
                            // Create a Complete button with text
                            Button completeButton = new Button("Complete");
                            completeButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 8;");
                            
                            ImageView deleteIcon = new ImageView(
                                    new Image(getClass().getResourceAsStream("/lk/wms/aquaflow/assets/icons8-delete-16.png"))
                            );

                            editIcon.setFitWidth(20);
                            editIcon.setFitHeight(20);
                            deleteIcon.setFitWidth(20);
                            deleteIcon.setFitHeight(20);

                            editIcon.setOnMouseClicked(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onEdit.accept(rowItem);
                            });
                            
                            completeButton.setOnAction(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onComplete.accept(rowItem);
                            });

                            deleteIcon.setOnMouseClicked(event -> {
                                T rowItem = getTableView().getItems().get(getIndex());
                                onDelete.accept(rowItem);
                            });

                            HBox hbox = new HBox(10, editIcon, completeButton, deleteIcon);
                            hbox.setPadding(new Insets(3));
                            setGraphic(hbox);
                        }
                        setText(null);
                    }
                };
            }
        };
    }
}
