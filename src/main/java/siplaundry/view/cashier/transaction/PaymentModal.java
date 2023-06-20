package siplaundry.view.cashier.transaction;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.kordamp.ikonli.javafx.FontIcon;
import retrofit2.Call;
import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;
import siplaundry.data.SessionData;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.repository.TransactionRepo;
import siplaundry.util.NumberUtil;
import siplaundry.view.print.ReceiptPrint;
import toast.Toast;
import toast.ToastType;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class PaymentModal {
    @FXML
    private HBox close_btn;
    @FXML
    private Text input_preview, customer_name, grand_total, payment_status, total_status;
    @FXML
    private TextField input_total;
    @FXML
    private FontIcon status_icon;

    private BorderPane shadowRoot;
    private List<TransactionDetailEntity> details;
    private CustomerEntity customer;
    private PaymentStatus paymentStatus = PaymentStatus.unpaid;
    private TransactionRepo transRepo = new TransactionRepo();
    private TransactionDetailRepo transDetailRepo = new TransactionDetailRepo();
    private int grandTotal = 0;
    private boolean isExpress;
    private Callable resetAction;

    public PaymentModal(BorderPane shadowRoot, List<TransactionDetailEntity> details, CustomerEntity customer, boolean isExpress, Callable resetAction) {
        this.shadowRoot = shadowRoot;
        this.details = details;
        this.customer = customer;
        this.isExpress = isExpress;
        this.resetAction = resetAction;

        for(TransactionDetailEntity detail: details) {
            this.grandTotal += detail.getSubtotal();
        }

        initModal();
    }

    @FXML
    void initialize() {
        customer_name.setText(this.customer.getname());
        grand_total.setText("Rp " + NumberUtil.rupiahFormat(this.grandTotal));
        total_status.setText("Rp " + NumberUtil.rupiahFormat(this.grandTotal));
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void saveTransaction() throws Exception {
        Date nowDate = new Date();

        Integer totalPay = NumberUtil.convertToInteger(input_total.getText());
        TransactionEntity transaction = new TransactionEntity();

        Calendar calender = Calendar.getInstance();
        calender.setTime(nowDate);
        calender.add(Calendar.HOUR_OF_DAY, (isExpress) ? 24 : 72);

        if(totalPay == null) totalPay = 0;

        transaction.settransactionDate(nowDate);
        transaction.setCustomerID(customer);
        transaction.setPaymentSatatus(paymentStatus);
        transaction.setpickupDate(calender.getTime());
        transaction.setRetard(0);
        transaction.setPaid_off(totalPay);
        transaction.setUserID(SessionData.user);
        transaction.setstatus(LaundryStatus.process);
        transaction.setIsExpress(this.isExpress);

        int tranId = transRepo.add(transaction);
        TransactionEntity trans = transRepo.get(tranId);

        saveDetails(trans);
        resetAction.call();
        printReceipt(trans);

        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
                .show(ToastType.SUCCESS, "Berhasil melakukan transaksi", null);

        closeModal();
    }

    @FXML
    void updatePreview() {
        Integer totalPay = NumberUtil.convertToInteger(input_total.getText());

        if(totalPay != null) {
            if(totalPay < 1) {
                payment_status.setText("Belum Bayar:");
                total_status.setText("Rp " + NumberUtil.rupiahFormat(grandTotal));
                total_status.setFill(Paint.valueOf("#E96479"));

                status_icon.setIconLiteral("bx-x");
                status_icon.setFill(Paint.valueOf("#E96479"));

                paymentStatus = PaymentStatus.unpaid;
            }

            if(totalPay < grandTotal) {
                payment_status.setText("Belum Lunas:");
                total_status.setText("Rp " + NumberUtil.rupiahFormat(grandTotal - totalPay));
                total_status.setFill(Paint.valueOf("#FFB74D"));

                status_icon.setIconLiteral("bx-minus");
                status_icon.setFill(Paint.valueOf("#FFB74D"));

                paymentStatus = PaymentStatus.instalment;
            }

            if(totalPay == grandTotal) {
                payment_status.setText("Lunas:");
                total_status.setText("Rp " + NumberUtil.rupiahFormat(grandTotal));
                total_status.setFill(Paint.valueOf("#7DB9B6"));

                status_icon.setIconLiteral("bx-check");
                status_icon.setFill(Paint.valueOf("#7DB9B6"));

                paymentStatus = PaymentStatus.paid;
                new Pulse(total_status).play();
            }

            if(totalPay > grandTotal) {
                payment_status.setText("Uang Kembali:");
                total_status.setText("Rp " + NumberUtil.rupiahFormat(totalPay - grandTotal));
                total_status.setFill(Paint.valueOf("#7DB9B6"));

                status_icon.setIconLiteral("bx-check-double");
                status_icon.setFill(Paint.valueOf("#7DB9B6"));

                paymentStatus = PaymentStatus.paid;
                new Pulse(total_status).play();
            }
        }

        if(totalPay == null) {
            payment_status.setText("Belum Bayar: ");
            total_status.setFill(Paint.valueOf("#E96479"));

            status_icon.setIconLiteral("bx-x");
            status_icon.setFill(Paint.valueOf("#E96479"));

            totalPay = 0;

            paymentStatus = PaymentStatus.unpaid;
        }


        input_preview.setText("Rp " + NumberUtil.rupiahFormat(totalPay));
    }

    void initModal() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/payment.fxml"));
        loader.setController(this);

        try {
            Parent root = loader.load();
            Stage modalStage = new Stage();
            Scene modalScene = new Scene(root);

            modalScene.setFill(Color.TRANSPARENT);
            modalStage.setScene(modalScene);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.setResizable(false);

            modalStage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            modalStage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }

    void saveDetails(TransactionEntity transaction) {
        for(TransactionDetailEntity detail: details) {
            detail.setTransaction(transaction);
            transDetailRepo.add(detail);
        }
    }

    void printReceipt(TransactionEntity transaction) {
        new ReceiptPrint(transaction, transDetailRepo.get(new HashMap<>() {{
            put("transaction_id", transaction.getid());
        }}));
    }
}
