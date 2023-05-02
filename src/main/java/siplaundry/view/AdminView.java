package siplaundry.view;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pages/admin.fxml"));
        Scene scene = new Scene(root);
        ObservableList<String> styles = scene.getStylesheets();

        styles.add(getClass().getResource("/style/main.css").toString());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
