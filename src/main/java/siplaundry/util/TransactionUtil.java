package siplaundry.util;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;

import java.util.ArrayList;
import java.util.List;

public class TransactionUtil {
    public static int getExistLaundry(LaundryEntity laundry, ArrayList<TransactionDetailEntity> details) {
        int launIndex = -1;

        for(int i = 0; i < details.size(); i++) {
            if (details.get(i).getLaundry().getid() == laundry.getid()) {
                launIndex = i;
                break;
            }
        }

        return launIndex;
    }

    public static HBox generateDetails(TransactionDetailEntity detail) {
        HBox container = new HBox();
        HBox itemContainer = new HBox();

        Text detailName = new Text(detail.getLaundry().getname());
        Text detailQty = new Text("x" + String.valueOf(detail.getQty()) + " " + detail.getLaundry().getunit());
        Text detailSubtotal = new Text("Rp " + NumberUtil.rupiahFormat(detail.getSubtotal()));

        itemContainer.setPrefWidth(200);
        itemContainer.setSpacing(10);

        container.getStyleClass().add("invoice-detail");
        detailName.getStyleClass().add("name");
        detailQty.getStyleClass().add("qty");

        detailSubtotal.setWrappingWidth(250);
        detailSubtotal.setTextAlignment(TextAlignment.RIGHT);

        itemContainer.getChildren().addAll(detailName, detailQty);
        container.getChildren().addAll(itemContainer, detailSubtotal);

        return container;
    }
}
