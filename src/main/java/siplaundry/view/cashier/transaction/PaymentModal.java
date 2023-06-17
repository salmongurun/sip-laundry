package siplaundry.view.cashier.transaction;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.util.NumberUtil;

import java.io.IOException;
import java.util.List;

public class PaymentModal {
    @FXML
    private HBox close_btn;
    @FXML
    private Text input_preview, customer_name, grand_total, payment_status, total_status;
    @FXML
    private TextField input_total;

    private BorderPane shadowRoot;
    private List<TransactionDetailEntity> details;
    private CustomerEntity customer;
    private int grandTotal = 0;
    public PaymentModal(BorderPane shadowRoot, List<TransactionDetailEntity> details, CustomerEntity customer) {
        this.shadowRoot = shadowRoot;
        this.details = details;
        this.customer = customer;

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
    void updatePreview() {
        Integer totalPay = NumberUtil.convertToInteger(input_total.getText());

        if(totalPay == null || totalPay < 1) {
            payment_status.setText("Belum Bayar:");
            total_status.setText("Rp " + NumberUtil.rupiahFormat(grandTotal));
        }

        if(totalPay != null && totalPay < grandTotal) {
            payment_status.setText("Belum Lunas:");
            total_status.setText("Rp " + NumberUtil.rupiahFormat(grandTotal - totalPay));
        }

        if(totalPay != null && totalPay == grandTotal) {
            payment_status.setText("Lunas:");
            total_status.setText("Rp " + NumberUtil.rupiahFormat(grandTotal));
            new Pulse(total_status).play();
        }

        if (totalPay != null && totalPay > grandTotal) {
            payment_status.setText("Uang Kembali:");
            total_status.setText("Rp " + NumberUtil.rupiahFormat(totalPay - grandTotal));
            new Pulse(total_status).play();
        }

        if(totalPay == null) totalPay = 0;
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
}
