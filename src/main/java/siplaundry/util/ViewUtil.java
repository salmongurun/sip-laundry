package siplaundry.util;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;
import siplaundry.data.AccountRole;
import siplaundry.data.SessionData;
import siplaundry.view.AdminView;
import siplaundry.view.CashierView;

import java.util.Set;

public class ViewUtil {
    public static <T extends Application> void authRedirector(Stage stage){
        AccountRole role = SessionData.user.getRole();

        try {
            if(role == AccountRole.admin) {
                stage.setTitle("Administrator - SIP Laundry");
                (new AdminView()).start(stage);
            }

            if(role == AccountRole.cashier) {
                stage.setTitle("Kasir - SIP Laundry");
                (new CashierView()).start(stage);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }
}
