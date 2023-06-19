package siplaundry.view.cashier.retard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.TransactionEntity;

import java.io.IOException;
import java.util.function.Consumer;

public class TransactionPopoverItem extends HBox {
    @FXML
    private Text transaction_name, transaction_customer;

    private TransactionEntity transaction;
    private Consumer<TransactionEntity> addTransaction;

    public TransactionPopoverItem(TransactionEntity transaction, Consumer<TransactionEntity> addTransaction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/retard/transaction-popover-item.fxml"));
        this.transaction = transaction;
        this.addTransaction = addTransaction;

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

        this.setOnMouseClicked(event -> {
            this.addTransaction.accept(transaction);
        });
    }
}
