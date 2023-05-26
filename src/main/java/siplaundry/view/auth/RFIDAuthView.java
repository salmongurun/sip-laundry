package siplaundry.view.auth;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import siplaundry.controller.auth.RFIDAuthController;
import siplaundry.controller.auth.UsernameVerifyController;

import java.io.IOException;

public class RFIDAuthView {
    public RFIDAuthView(Node shadowRoot, Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/login-rfid.fxml"));
        loader.setController(new RFIDAuthController(primaryStage));

        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);

            stage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            stage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }
}
