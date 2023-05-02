package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import siplaundry.controller.admin.DashboardController;

import java.io.IOException;

public class DashboardView extends AnchorPane {
    public DashboardView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/dashboard.fxml"));

        loader.setRoot(this);
        loader.setController(new DashboardController());
        loader.load();
    }
}
