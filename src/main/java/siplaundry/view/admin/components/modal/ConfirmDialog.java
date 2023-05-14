package siplaundry.view.admin.components.modal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;

public class ConfirmDialog {
    @FXML
    private HBox cancel_btn, confirm_btn;
    private Node shadowRoot;
    private Runnable action;
    public ConfirmDialog(Node shadowRoot, Runnable action) {
        this.shadowRoot = shadowRoot;
        this.action = action;

        initDialog();
    }

    @FXML
    void closeDialog() {
        Window window = cancel_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void confirmAction() {
        action.run();
        closeDialog();
    }

    private void initDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/util/confirm-dialog.fxml"));
        loader.setController(this);

        try {
            Parent root = loader.load();
            Stage dialStage = new Stage();
            Scene dialScene = new Scene(root);

            dialScene.setFill(Color.TRANSPARENT);
            dialStage.setScene(dialScene);
            dialStage.initModality(Modality.APPLICATION_MODAL);
            dialStage.initStyle(StageStyle.TRANSPARENT);
            dialStage.setResizable(false);

            dialStage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            dialStage.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
