package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import siplaundry.controller.admin.ReportController;

import java.io.IOException;

public class ReportView extends AnchorPane {
    public ReportView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/report.fxml"));

        loader.setRoot(this);
        loader.setController(new ReportController());
        loader.load();
    }
}
