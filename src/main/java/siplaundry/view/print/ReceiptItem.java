package siplaundry.view.print;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.util.NumberUtil;

import java.io.IOException;

public class ReceiptItem extends HBox {
    @FXML
    private Text laundry_name, laundry_qty, laundry_price, laundry_subtotal;

    private TransactionDetailEntity detail;

    public ReceiptItem(TransactionDetailEntity detail) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/print/receipt-detail.fxml"));
        this.detail = detail;

        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        laundry_name.setText(this.detail.getLaundry().getname());
        laundry_qty.setText("x" + String.valueOf(this.detail.getQty()));
        laundry_price.setText(NumberUtil.rupiahFormat(this.detail.getLaundry().getcost()));
        laundry_subtotal.setText(NumberUtil.rupiahFormat(this.detail.getSubtotal()));
    }
}
