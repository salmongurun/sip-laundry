package siplaundry.view.cashier;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import siplaundry.controller.cashier.ReportController;

public class ReportView extends AnchorPane {

    public ReportView() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/report.fxml"));

        loader.setRoot(this);
        loader.setController(new ReportController());
        loader.load();
    }
    
}
