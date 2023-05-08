package siplaundry.view.admin.components.column;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;

import java.io.IOException;

public class AccountColumn extends HBox {
    @FXML
    private Text txt_username, txt_phone, txt_fullname, txt_role;

    @FXML
    private HBox role_background;
    private UserEntity user;

    public AccountColumn(UserEntity user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/account/column.fxml"));

        this.user = user;
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        setRoleColor();

        txt_fullname.setText(user.getFullname());
        txt_username.setText(user.getUsername());
        txt_phone.setText(user.getPhone());
        txt_role.setText(user.getRole().toString());
    }

    void setRoleColor() {
        if(user.getRole() == AccountRole.cashier) {
            txt_role.setStyle("-fx-fill: #6A9A98;");
            role_background.setStyle(role_background.getStyle() + "-fx-background-color: #f0f4f4;");
        }
    }
}
