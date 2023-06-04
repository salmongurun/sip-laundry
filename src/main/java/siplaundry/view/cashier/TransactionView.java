package siplaundry.view.cashier;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.cashier.TransactionController;

public class TransactionView extends AnchorPane{

    public TransactionView(BorderPane parent_root) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction.fxml"));

        loader.setRoot(this);
        loader.setController(new TransactionController(parent_root));
        loader.load();
    }
}
