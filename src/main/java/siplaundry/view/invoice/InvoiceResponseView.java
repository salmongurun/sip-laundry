package siplaundry.view.invoice;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.invoice.InvoiceDetailController;
import siplaundry.controller.invoice.InvoiceResponseController;
import siplaundry.entity.TransactionEntity;

import java.io.IOException;

public class InvoiceResponseView extends AnchorPane {
    public InvoiceResponseView(BorderPane parent_root, BorderPane shadow_root, TransactionEntity transaction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/invoice/invoice-response.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(new InvoiceResponseController(parent_root, shadow_root, transaction));
            loader.load();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
