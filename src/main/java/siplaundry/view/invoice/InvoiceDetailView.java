package siplaundry.view.invoice;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.invoice.InvoiceDetailController;
import siplaundry.entity.TransactionEntity;

import java.io.IOException;

public class InvoiceDetailView extends AnchorPane {
    public InvoiceDetailView(BorderPane parent_root, BorderPane shadow_root, TransactionEntity transaction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/invoice/invoice-detail.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(new InvoiceDetailController(parent_root, shadow_root, transaction));
            loader.load();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
