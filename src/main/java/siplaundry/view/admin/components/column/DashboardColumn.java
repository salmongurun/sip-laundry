package siplaundry.view.admin.components.column;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.LaundryDashboardEntity;
import siplaundry.util.NumberUtil;

import java.io.IOException;

public class DashboardColumn extends HBox {
    @FXML
    private Text laundry_name, laundry_cost, laundry_usage;

    private LaundryDashboardEntity laundry;

    public DashboardColumn(LaundryDashboardEntity laundry) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/dashboard/column.fxml"));
        this.laundry = laundry;

        try {
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        laundry_name.setText(laundry.getLaundry().getname());
        laundry_cost.setText("Rp " + NumberUtil.rupiahFormat(laundry.getLaundry().getcost()));
        laundry_usage.setText(laundry.getUsage() + "x");
    }
}
