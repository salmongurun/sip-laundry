package siplaundry.view.util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class EmptyData extends VBox {
    @FXML
    private HBox action_btn;
    @FXML
    private ImageView display_image;
    @FXML
    private Text display_text;
    private boolean isSearching;
    private Runnable action;
    public EmptyData(Runnable action, String keyword) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/util/empty-data.fxml"));
        this.action = action;
        this.isSearching = this.isSearching(keyword);

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        Image emptyResultImg = new Image(getClass().getResource("/images/empty-result.png").toString());

        action_btn.setOnMouseClicked(event -> this.action.run());

        if(this.isSearching) {
            display_image.setImage(emptyResultImg);
            display_text.setText("Data yang anda cari tidak dapat ditemukan");
            action_btn.setVisible(false);
        }
    }

    private boolean isSearching(String keyword) {
        return !keyword.isEmpty();
    }
}
