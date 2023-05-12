package siplaundry.view.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.admin.AccountController;

import java.io.IOException;

public class AccountView extends AnchorPane {

    private Runnable showTable;

    public AccountView(BorderPane shadow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/account.fxml"));

        loader.setRoot(this);
        loader.setController(new AccountController(shadow));
        loader.load();
    }
}
