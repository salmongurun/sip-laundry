package siplaundry.controller.invoice;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.TransactionEntity;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;

public class InvoiceDetailController {
    @FXML
    private Text transaction_name, customer_name, cashier_name, transaction_date, due_date, total_text, status_text, payment_text;
    @FXML
    private HBox payment_background, status_background;
    private BorderPane parent_root, shadow_root;
    private TransactionEntity transaction;

    public InvoiceDetailController(BorderPane parentRoot, BorderPane shadowRoot, TransactionEntity transaction) {
        this.parent_root = parentRoot;
        this.shadow_root = shadowRoot;
        this.transaction = transaction;
    }

    @FXML
    void initialize() {
        transaction_name.setText("Pesanan#" + transaction.getid());
        customer_name.setText(transaction.getCustomerID().getname());
        cashier_name.setText(transaction.getUserID().getFullname());
        total_text.setText("Rp " + NumberUtil.rupiahFormat(transaction.getamount()));
        transaction_date.setText(ViewUtil.formatDate(transaction.gettransactionDate(), "dd/MM/YYYY"));
        due_date.setText(ViewUtil.formatDate(transaction.getpickupDate(), "dd/MM/YYYY"));

        paymentStatusColor();
        setTransactionColor();
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
