package siplaundry.controller.invoice;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.AccountRole;
import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;
import siplaundry.data.SessionData;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.repository.TransactionRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.TransactionUtil;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.TransactionView;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.invoice.InvoiceDetailView;
import toast.Toast;
import toast.ToastType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class InvoiceResponseController {
    @FXML
    private Text transaction_name, customer_name, cashier_name, transaction_date, due_date, total_text, status_text, payment_text, retard_text, amount_due_text;
    @FXML
    private HBox payment_background, status_background, retard_element, amount_due_container, taken_button;
    @FXML
    private VBox detail_container;

    private BorderPane parent_root, shadow_root;
    private TransactionEntity transaction;
    private TransactionDetailRepo detailRepo = new TransactionDetailRepo();

    public InvoiceResponseController(BorderPane parentRoot, BorderPane shadowRoot, TransactionEntity transaction) {
        this.parent_root = parentRoot;
        this.shadow_root = shadowRoot;
        this.transaction = transaction;
    }

    @FXML
    void initialize() {
        transaction_name.setText("Transaksi#" + transaction.getid());
        customer_name.setText(transaction.getCustomerID().getname());
        cashier_name.setText(transaction.getUserID().getFullname());
        total_text.setText("Rp " + NumberUtil.rupiahFormat(transaction.getamount()));
        transaction_date.setText(ViewUtil.formatDate(transaction.gettransactionDate(), "dd/MM/YYYY"));
        due_date.setText(ViewUtil.formatDate(transaction.getpickupDate(), "dd/MM/YYYY"));
        retard_text.setText("Tertunda " + transaction.getRetard() + " Hari");
        amount_due_text.setText("Rp " + NumberUtil.rupiahFormat(transaction.getamount() - transaction.getPaid_off()));

        if(transaction.getRetard() < 1) retard_element.setVisible(false);
        if(transaction.getPaid_off() >= transaction.getamount())
            amount_due_container.setVisible(false);
        if(transaction.getstatus() == LaundryStatus.taken)
            taken_button.setVisible(false);

        paymentStatusColor();
        setTransactionColor();
        addDetailElements();
    }

    @FXML
    void backAction() throws IOException {
        if(SessionData.user.getRole().equals(AccountRole.cashier)){
            parent_root.setCenter(new siplaundry.view.cashier.TransactionView(parent_root, shadow_root));
            return;
        }

        parent_root.setCenter(new TransactionView(parent_root, shadow_root));
    }

    @FXML
    void takenAction() {
        new ConfirmDialog(shadow_root, () -> {
            transaction.setstatus(LaundryStatus.taken);
            transaction.setPaymentSatatus(PaymentStatus.paid);

            new TransactionRepo().Update(transaction);
            new Toast((AnchorPane) shadow_root.getScene().getRoot())
                    .show(ToastType.SUCCESS, "Berhasil mengupdate transaksi", null);

            parent_root.setCenter(new InvoiceDetailView(parent_root, shadow_root, transaction));
        }, "Pastikan customer telah membayar lunas", true);
    }

    void addDetailElements() {
        List<TransactionDetailEntity> details = detailRepo.get(new HashMap<>() {{
            put("transaction_id", transaction.getid());
        }});

        for(TransactionDetailEntity detail: details) {
            detail_container.getChildren().add(TransactionUtil.generateDetails(detail));
        }
    }

    void paymentStatusColor() {
        switch (transaction.getPaymentStatus()) {
            case paid -> {
                payment_background.getStyleClass().add("badge-success");
                payment_text.setText("Lunas");
            }

            case unpaid -> {
                payment_background.getStyleClass().add("badge-danger");
                payment_text.setText("Belum Dibayar");
            }

            case instalment -> {
                payment_background.getStyleClass().add("badge-warning");
                payment_text.setText("Belum Lunas");
            }
        }
    }

    void setTransactionColor() {
        switch (transaction.getstatus()) {
            case process -> {
                status_background.getStyleClass().add("badge-primary");
                status_text.setText("Diproses");
            }

            case taken -> {
                status_background.getStyleClass().add("badge-success");
                status_text.setText("Diambil");
            }

            case finish -> {
                status_background.getStyleClass().add("badge-success");
                status_text.setText("Selesai");
            }

            case canceled -> {
                status_background.getStyleClass().add("badge-danger");
                status_text.setText("Dibatalkan");
            }
        }
    }
}
