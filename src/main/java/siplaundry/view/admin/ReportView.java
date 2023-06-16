package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.admin.ReportController;

import java.io.IOException;

public class ReportView extends AnchorPane {
    public ReportView(BorderPane shadowRoot) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/report.fxml"));

        loader.setRoot(this);
        loader.setController(new ReportController(shadowRoot));
        loader.load();
    }
}
