package siplaundry.view.cashier.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import siplaundry.entity.TransactionDetailEntity;

import java.io.IOException;

public class CartItem extends AnchorPane {
    public CartItem(TransactionDetailEntity detail) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/cart-item.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {

    }
}
