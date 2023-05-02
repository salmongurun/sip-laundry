package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;

public class DashboardController {
    @FXML
    Text greet_name;

    @FXML
    void initialize() {
        greet_name.setText(SessionData.user.getFullname());
    }
}
