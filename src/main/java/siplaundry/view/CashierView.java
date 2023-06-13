package siplaundry.view;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CashierView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("/pages/cashier.fxml"));
      Scene scene = new Scene(root);
      ObservableList<String> styles = scene.getStylesheets();

      styles.add(getClass().getResource("/style/main.css").toString());

      primaryStage.setScene(scene);
      primaryStage.show();
    }
    
}
