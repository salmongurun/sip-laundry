package siplaundry.view.cashier;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.cashier.ReportController;

public class ReportView extends AnchorPane {

    public ReportView(BorderPane shadow) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/report.fxml"));

        loader.setRoot(this);
        loader.setController(new ReportController(shadow));
        loader.load();
    }
    
}
