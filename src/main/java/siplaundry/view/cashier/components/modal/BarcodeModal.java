package siplaundry.view.cashier.components.modal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.kordamp.ikonli.javafx.FontIcon;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.view.cashier.components.BarcodeItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BarcodeModal {
    @FXML
    private HBox close_btn, notify_container;
    @FXML
    private VBox transaction_container;
    @FXML
    private FontIcon notify_icon;
    @FXML
    private Text notify_text;
    @FXML
    private TextField barcode_input;

    private BorderPane shadowRoot, parentRoot;
    private TransactionRepo transRepo = new TransactionRepo();

    public BarcodeModal(BorderPane parentRoot, BorderPane shadowRoot) {
        this.shadowRoot = shadowRoot;
        this.parentRoot = parentRoot;

        initModal();
    }

    @FXML
    void searchAction() {
        String barcodeVal = barcode_input.getText();
        transaction_container.getChildren().clear();

        if(barcodeVal.isBlank() || barcodeVal.length() < 9) {
            notify_container.setVisible(true);
            notify_text.setText("Menunggu barcode...");
            notify_icon.setIconLiteral("bx-barcode");

            return;
        }

        String transId = barcodeVal.substring(0, barcodeVal.length() - 8);
        List<TransactionEntity> transactions = transRepo.get(new HashMap<>() {{
            put("transaction_id", transId);
        }});

        if(transactions.isEmpty()) {
            notify_container.setVisible(true);
            notify_text.setText("Transaksi tidak ditemukan");
            notify_icon.setIconLiteral("bx-notification-off");

            return;
        }

        notify_container.setVisible(false);
        transaction_container.getChildren().add(
            new BarcodeItem(parentRoot, shadowRoot, close_btn.getScene().getWindow(), transactions.get(0))
        );
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void initModal(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/barcode/barcode-modal.fxml"));
        loader.setController(this);

        try {
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

        } catch (IOException e) { e.printStackTrace(); }
    }
}
