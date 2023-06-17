package siplaundry.view.util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.io.IOException;

public class WarningModal {
    @FXML
    private Text message_text;

    private String message;

    public WarningModal(BorderPane shadowRoot, String message) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/util/warning-modal.fxml"));
        this.message = message;

        try {
            loader.setController(this);
            Parent root = loader.load();

            Stage modalStage = new Stage();
            Scene modalScene = new Scene(root);

            modalScene.setFill(Color.TRANSPARENT);
            modalStage.setScene(modalScene);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.setResizable(false);

            modalStage.setOnCloseRequest(evt -> shadowRoot.setVisible(false));

            shadowRoot.setVisible(true);
            modalStage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        message_text.setText(message);
    }

    @FXML
    void closeModal() {
        Window window = message_text.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
