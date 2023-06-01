package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.admin.TransactionController;

import java.io.IOException;

public class TransactionView extends AnchorPane {
    public TransactionView(BorderPane shadow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/transaction.fxml"));

        loader.setRoot(this);
        loader.setController(new TransactionController(shadow));
        loader.load();
    }
}
