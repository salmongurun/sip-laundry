package siplaundry.controller.cashier;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.repository.TransactionRepo;

public class DashboarController {
    @FXML
    private Text greet_name, txt_cust, txt_process, txt_taken;

    TransactionRepo transRepo = new TransactionRepo();

    private String cust, process, taken;

    @FXML
    void initialize() {
        greet_name.setText(SessionData.user.getFullname());

        cust = transRepo.DashboardCount("count", "customer_id");
        process = transRepo.DashboardCount("count", "transaction_id", new HashMap<>(){{
            put("status", "process");
        }});
        taken = transRepo.DashboardCount("count", "transaction_id", new HashMap<>(){{
            put("status", "taken");
        }});

        txt_cust.setText(cust);
        txt_process.setText(process);
        txt_taken.setText(taken);
    }
}
