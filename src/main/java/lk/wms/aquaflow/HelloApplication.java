package lk.wms.aquaflow;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("AquaFlow WMS");
      //  stage.getIcons().add(new Image(getClass().getResource("/lk/aquaflowwms/assets/waterboard.jpg").toExternalForm()));
        stage.show();

    }
}