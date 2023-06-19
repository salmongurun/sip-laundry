package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.admin.ReportController;
import siplaundry.controller.admin.ReportExpenseController;

import java.io.IOException;

public class ReportExpenseView extends AnchorPane {
    public ReportExpenseView(BorderPane parentRoot, BorderPane shadowRoot) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/report-expense.fxml"));

        loader.setRoot(this);
        loader.setController(new ReportExpenseController(parentRoot, shadowRoot));
        loader.load();
    }
}
