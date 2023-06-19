package siplaundry.controller.cashier;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import siplaundry.entity.TransactionEntity;
import siplaundry.entity.WhatsappMessage;
import siplaundry.repository.TransactionRepo;
import siplaundry.service.WhatsAppService;
import siplaundry.util.MessageUtil;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.cashier.retard.RetardItem;
import siplaundry.view.cashier.retard.TransactionPopover;
import siplaundry.view.util.WarningModal;
import toast.Toast;
import toast.ToastType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RetardController {
    @FXML
    private HBox choose_transaction;
    @FXML
    private VBox transactions_container;
    @FXML
    private TextField retard_day;
    @FXML
    private TextArea retard_note;

    private Integer durationDay;
    private BorderPane shadow_root;
    private PopOver transPopover = new PopOver();
    private List<TransactionEntity> transactions = new ArrayList<>();
    private TransactionRepo transRepo = new TransactionRepo();

    public RetardController(BorderPane shadowRoot) {
        this.shadow_root = shadowRoot;
    }

    @FXML
    void showTransactionList() {
        transPopover.setContentNode(new TransactionPopover(this::addTransaction, shadow_root));
        transPopover.show(choose_transaction);
        transPopover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    }

    @FXML
    void askSaveRetard() {
        durationDay = NumberUtil.convertToInteger(retard_day.getText());

        if(durationDay == null || durationDay < 1) {
            new WarningModal(shadow_root, "Durasi keterlambatan harus diisi");
            return;
        }

        if(transactions.isEmpty()) {
            new WarningModal(shadow_root, "Anda harus memilih transaksi terlebih dahulu");
            return;
        }

        new ConfirmDialog(shadow_root, this::saveRetard, "Anda akan menambahkan keterlambatan pada pesanan terilih", true);
    }

    void saveRetard() {
        durationDay = NumberUtil.convertToInteger(retard_day.getText());

        for(TransactionEntity transaction: transactions) {
            WhatsAppService message = new WhatsAppService();

            message.send(new WhatsappMessage(
                transaction.getCustomerID().getphone(),
                MessageUtil.retardMessGen(durationDay, retard_note.getText())
            ));

            transaction.setRetard(transaction.getRetard() + durationDay);
            transRepo.Update(transaction);
        }

        Toast toast = new Toast((AnchorPane) shadow_root.getScene().getRoot());
        toast.show(ToastType.SUCCESS, "Berhasil menambahkan keterlambatan", null);

        clearAllEntries();
    }

    void addTransaction(TransactionEntity transaction) {
        if(isTransactionExist(transaction) >= 0) {
            new WarningModal(shadow_root, "Transaksi sudah terdapat dalam list");
            return;
        }

        transactions.add(transaction);
        transPopover.hide();

        showItems();
    }

    void showItems() {
        transactions_container.getChildren().clear();

        for(TransactionEntity transaction: transactions)
            transactions_container.getChildren().add(new RetardItem(transaction, this::removeItem));
    }

    int isTransactionExist(TransactionEntity trans) {
        int index = -1;

        for(int i = 0; i < transactions.size(); i++) {
            TransactionEntity transaction = transactions.get(i);

            if(transaction.getid() == trans.getid()) {
                index = i;
                break;
            }
        }

        return index;
    }

    void removeItem(TransactionEntity transaction) {
        int index = isTransactionExist(transaction);

        transactions.remove(index);
        showItems();
    }

    void clearAllEntries() {
        transactions.clear();
        retard_day.clear();
        retard_note.clear();

        showItems();
    }
}
