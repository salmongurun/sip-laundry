package siplaundry.util;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import siplaundry.data.AccountRole;
import siplaundry.data.SessionData;
import siplaundry.data.SortingOrder;
import siplaundry.view.AdminView;
import siplaundry.view.CashierView;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static SortingOrder switchOrderIcon(SortingOrder sortOrder, FontIcon sortIcon) {
        if(sortOrder == SortingOrder.DESC) {
            sortOrder = SortingOrder.ASC;
            sortIcon.setIconLiteral("bx-sort-down");
        } else {
            sortOrder = SortingOrder.DESC;
            sortIcon.setIconLiteral("bx-sort-up");
        }

        return sortOrder;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
