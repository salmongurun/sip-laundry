package siplaundry.view.cashier;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.cashier.DashboarController;

public class DashboardView extends AnchorPane {
    public DashboardView(BorderPane shadow) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/dashboardCashier.fxml"));

        loader.setRoot(this);
        loader.setController(new DashboarController(shadow));
        loader.load();
    }
}
