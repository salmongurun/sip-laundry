package siplaundry.view.cashier.transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.util.NumberUtil;

import java.io.IOException;
import java.util.function.Consumer;

public class CartItem extends AnchorPane {
    @FXML
    private Text laundry_name, laundry_qty, laundry_subtotal, laundry_unit;
    @FXML
    private TransactionDetailEntity detail;
    @FXML
    private HBox add_btn, red_btn, icon_background;
    @FXML
    private FontIcon laundry_icon;

    private Consumer<TransactionDetailEntity> addAction, redAction;

    public CartItem(TransactionDetailEntity detail, Consumer<TransactionDetailEntity> addAction, Consumer<TransactionDetailEntity> redAction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/cart-item.fxml"));

        this.detail = detail;
        this.addAction = addAction;
        this.redAction = redAction;

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        laundry_name.setText(this.detail.getLaundry().getname());
        laundry_qty.setText(String.valueOf(this.detail.getQty()));
        laundry_subtotal.setText("Rp " + NumberUtil.rupiahFormat(this.detail.getSubtotal()));
        laundry_unit.setText(this.detail.getLaundry().getunit().toString());

        if(this.detail.getLaundry().getIsExpress()) {
            laundry_icon.setIconLiteral("bxs-zap");
            laundry_icon.setFill(Paint.valueOf("#ffb74d"));

            icon_background.setStyle(icon_background.getStyle() + " -fx-background-color: #FEECD2;");
        }

        add_btn.setOnMouseClicked(event -> {
            this.addAction.accept(this.detail);
        });

        red_btn.setOnMouseClicked(event -> {
            this.redAction.accept(this.detail);
        });
    }
}
