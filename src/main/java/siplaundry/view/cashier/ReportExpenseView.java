package siplaundry.view.cashier;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.cashier.ReportExpenseController;

public class ReportExpenseView extends AnchorPane {
     public ReportExpenseView(BorderPane parentRoot, BorderPane shadowRoot) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/reportExp.fxml"));

        loader.setRoot(this);
        loader.setController(new ReportExpenseController(parentRoot, shadowRoot));
        loader.load();
    }
}
