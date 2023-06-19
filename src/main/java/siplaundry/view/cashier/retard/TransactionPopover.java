package siplaundry.view.cashier.retard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class TransactionPopover extends AnchorPane {
    @FXML
    private VBox transaction_list;
    @FXML
    private TextField txt_keyword;

    private Consumer<TransactionEntity> addTransaction;
    private BorderPane shadow_root;
    private TransactionRepo transRepo = new TransactionRepo();

    public TransactionPopover(Consumer<TransactionEntity> addTransaction, BorderPane shadowRoot) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/retard/transaction-popover.fxml"));
        this.addTransaction = addTransaction;
        this.shadow_root = shadowRoot;

        try {
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        showTransactions(transRepo.get());
    }

    @FXML
    void searchAction() {
        showTransactions(transRepo.search(new HashMap<>() {{
            put("transaction_id", txt_keyword.getText());
        }}));
    }

    void showTransactions(List<TransactionEntity> transactions) {
        if(transactions == null) return;
        transaction_list.getChildren().clear();

        for(TransactionEntity transaction: transactions) {
            transaction_list.getChildren().add(new TransactionPopoverItem(
                transaction,
                this.addTransaction
            ));
        }
    }
}
