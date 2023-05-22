package siplaundry.view.auth;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginView extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pages/auth/login2.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/main.css").toString());
        scene.getStylesheets().add(getClass().getResource("/style/auth/login.css").toString());

        primaryStage.setTitle("Login - SIP Laundry");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
