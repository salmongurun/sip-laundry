package siplaundry.view.cashier.retard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.TransactionEntity;

import java.io.IOException;
import java.util.function.Consumer;

public class RetardItem extends HBox {
    @FXML
    private Text transaction_name, transaction_customer;
    @FXML
    private HBox delete_btn;

    private TransactionEntity transaction;
    private Consumer<TransactionEntity> removeAction;

    public RetardItem(TransactionEntity transaction, Consumer<TransactionEntity> removeAction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/retard/retard-item.fxml"));
        this.transaction = transaction;
        this.removeAction = removeAction;

        try {
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        transaction_name.setText("Transaksi#" + transaction.getid());
        transaction_customer.setText("Cust: " + transaction.getCustomerID().getname());

        delete_btn.setOnMouseClicked(event -> {
            removeAction.accept(transaction);
        });
    }
}
