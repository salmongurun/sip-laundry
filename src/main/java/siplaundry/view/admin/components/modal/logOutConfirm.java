package siplaundry.view.admin.components.modal;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class logOutConfirm {
    @FXML
    private HBox cancel_btn, confirm_btn;

    private Node shadowRoot;

    private Runnable action;

    public logOutConfirm(Node shadowRoot, Runnable action){
        this.shadowRoot = shadowRoot;
        this.action = action;

        initDialog();
    }

    @FXML
    void closeDialog(){
        Window window = cancel_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void confirmAction(){
        action.run();
        closeDialog();
    }

    private void initDialog(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/util/logOut-confirm.fxml"));
        loader.setController(this);

        try {
            Parent root = loader.load();
            Stage dialstage = new Stage();
            Scene dialScene = new Scene(root);

            dialScene.setFill(Color.TRANSPARENT);
            dialstage.setScene(dialScene);
            dialstage.initModality(Modality.APPLICATION_MODAL);
            dialstage.initStyle(StageStyle.TRANSPARENT);
            dialstage.setResizable(false);

            dialstage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            dialstage.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

}
