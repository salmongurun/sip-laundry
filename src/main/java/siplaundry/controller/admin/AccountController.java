package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.view.admin.components.column.AccountColumn;

import java.io.IOException;
import java.util.List;

public class AccountController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_account;

    @FXML
    private VBox account_table;

    @FXML
    private Text total_text;

    private UsersRepo userRepo = new UsersRepo();

    public AccountController(BorderPane shadow) {
        this.shadowRoot = shadow;
    }

    @FXML
    void initialize() {
        List<UserEntity> users = userRepo.get();

        for(UserEntity user: users) {
            this.account_table.getChildren().add(new AccountColumn(user));
        }

        total_text.setText("Menampilkan total "+ users.size() +" data akun");
    }

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
