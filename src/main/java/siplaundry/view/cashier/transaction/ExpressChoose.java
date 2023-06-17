package siplaundry.view.cashier.transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;
import java.util.concurrent.Callable;

public class ExpressChoose {
    @FXML
    private HBox accept_btn;

    private BorderPane shadow_root;
    private Callable acceptMethod, rejectMethod;

    public ExpressChoose(BorderPane shadowRoot, Callable acceptMethod, Callable rejectMethod) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/express-choose.fxml"));
        this.shadow_root = shadowRoot;
        this.acceptMethod = acceptMethod;
        this.rejectMethod = rejectMethod;

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
    void closeModal() {
        Window window = accept_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void acceptExpress() throws Exception {
        acceptMethod.call();
        closeModal();
    }

    @FXML
    void rejectExpress() throws Exception {
        rejectMethod.call();
        closeModal();
    }
}
