package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AccountController {
    private BorderPane shadowRoot;
    public AccountController(BorderPane shadow) {
        this.shadowRoot = shadow;
    }
    @FXML
    private HBox btn_add_account;

    @FXML
    void showAddAccount(MouseEvent event) {
        showModal("Tambah Akun");
    }

    private void showModal(String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/pages/admin/account/add.fxml"));
            Stage modalStage = new Stage();
            Scene modalScene = new Scene(root);

            modalScene.setFill(Color.TRANSPARENT);


            modalStage.setTitle(title);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.setResizable(false);
            modalStage.setScene(modalScene);
            modalStage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            modalStage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }
}
