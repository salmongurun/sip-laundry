package siplaundry.view.cashier.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.entity.TransactionEntity;
import siplaundry.view.invoice.InvoiceDetailView;

import java.io.IOException;

public class BarcodeItem extends HBox {
    @FXML
    private Text transaction_name, getTransaction_customer;

    private TransactionEntity transaction;
    private BorderPane parentRoot, shadowRoot;
    private Window modal;

    public BarcodeItem(BorderPane parentRoot, BorderPane shadowRoot, Window modal, TransactionEntity transaction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/barcode/barcode-item.fxml"));
        this.transaction = transaction;
        this.parentRoot = parentRoot;
        this.shadowRoot = shadowRoot;
        this.modal = modal;

        try {
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        transaction_name.setText("Transaction#" + transaction.getid());
        getTransaction_customer.setText("Cust: " + transaction.getCustomerID().getname());
    }

    @FXML
    void nextAction() {
        modal.fireEvent(new WindowEvent(modal, WindowEvent.WINDOW_CLOSE_REQUEST));
        parentRoot.setCenter(new InvoiceDetailView(parentRoot, shadowRoot, transaction));
    }
}
