package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import siplaundry.controller.admin.PriceController;

import java.io.IOException;

public class PriceView extends AnchorPane {
    public PriceView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/price.fxml"));

        loader.setRoot(this);
        loader.setController(new PriceController());
        loader.load();
    }
}
