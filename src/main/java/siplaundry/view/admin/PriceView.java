package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.admin.PriceController;

import java.io.IOException;

public class PriceView extends AnchorPane {
    public PriceView(BorderPane shadow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/price.fxml"));

        loader.setRoot(this);
        loader.setController(new PriceController(shadow));
        loader.load();
    }
}
