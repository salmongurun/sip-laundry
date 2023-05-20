package siplaundry.util;

import javafx.application.Application;
import javafx.stage.Stage;
import siplaundry.data.AccountRole;
import siplaundry.data.SessionData;
import siplaundry.view.AdminView;
import siplaundry.view.CashierView;

public class ViewUtil {
    public <T extends Application> void authRedirector(Stage stage) throws Exception{
        AccountRole role = SessionData.user.getRole();

        if(role == AccountRole.admin) (new AdminView()).start(stage);
        if(role == AccountRole.cashier) (new CashierView()).start(stage);
    }
}
