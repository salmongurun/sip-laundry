package siplaundry.view.admin.components.column;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.LaundryStatus;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportColumn extends HBox {
    @FXML
    private Text transaction_name, transaction_date, transaction_total, status_text;
    @FXML
    private VBox items_container;
    @FXML
    private HBox status_background;

    private String status = "Diproses";
    private BorderPane shadow_root;
    private TransactionEntity transaction;
    private List<TransactionDetailEntity> details;

    public ReportColumn(BorderPane shadowRoot, TransactionEntity transaction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/report/column.fxml"));

        this.shadow_root = shadowRoot;
        this.transaction = transaction;
        this.details = new TransactionDetailRepo().get(new HashMap<>() {{
            put("transaction_id", transaction.getid());
        }});

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        transaction_name.setText("Transaksi#" + transaction.getid());
        transaction_date.setText(ViewUtil.formatDate(transaction.gettransactionDate(), "dd/MM/YYYY"));
        transaction_total.setText("Rp " + NumberUtil.rupiahFormat(transaction.getamount()));

        setItems();
        setStatusColor();

        status_text.setText(status);
    }

    private void setItems() {
        for(int i = 0; i < details.size(); i++) {
            TransactionDetailEntity detail = details.get(i);

            Text name = new Text(detail.getLaundry().getname());
            Text qty = new Text(String.valueOf(detail.getQty()) + " " + detail.getLaundry().getunit());
            Text subtotal = new Text("Rp " + NumberUtil.rupiahFormat(detail.getSubtotal()));

            name.setWrappingWidth(150);
            qty.setWrappingWidth(100);
            subtotal.setWrappingWidth(110);

            HBox item = bindItems(new ArrayList<>() {{
                add(name); add(qty); add(subtotal);
            }});

            if(i < (details.size() - 1)) item.getStyleClass().add("item-container");
            items_container.getChildren().add(item);
        }
    }

    private HBox bindItems(List<Text> elements) {
        HBox container = new HBox();
        container.setSpacing(30);

        for(Text element: elements) {
            element.getStyleClass().add("item-text");
            container.getChildren().add(element);
        }

        return container;
    }

    void setStatusColor(){
        if(transaction.getstatus() == LaundryStatus.finish) {
            status = "Selesai";
            status_text.setStyle("-fx-fill: #278AA6;");
            status_background.setStyle(status_background.getStyle() + "-fx-background-color: #E0F4FB;");
        } else if(transaction.getstatus() == LaundryStatus.canceled) {
            status = "Dibatalkan";
            status_text.setStyle("-fx-fill: #F45050;");
            status_background.setStyle(status_background.getStyle() + "-fx-background-color: #fef4f4;");
        } else if(transaction.getstatus() == LaundryStatus.taken){
            status = "Diambil";
            status_text.setStyle("-fx-fill: #6A9A98;");
            status_background.setStyle(status_background.getStyle() + "-fx-background-color: #f0f4f4;");
        }
    }
}
