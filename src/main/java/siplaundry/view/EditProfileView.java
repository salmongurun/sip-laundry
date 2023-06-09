package siplaundry.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.auth.EditProfileController;

public class EditProfileView extends AnchorPane{
    public EditProfileView(BorderPane shadow) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/editProfile.fxml"));

        loader.setRoot(this);
        loader.setController(new EditProfileController(shadow));
        loader.load();
    }
}
