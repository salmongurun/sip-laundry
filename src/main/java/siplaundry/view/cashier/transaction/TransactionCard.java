package siplaundry.view.cashier.transaction;

import animatefx.animation.FadeIn;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import siplaundry.entity.LaundryEntity;

import java.io.IOException;

public class TransactionCard extends AnchorPane {
    @FXML
    private Text laundry_name;
    @FXML
    private HBox express_badge;

    private LaundryEntity laundry;
    public TransactionCard(LaundryEntity laundry) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/card.fxml"));
        this.laundry = laundry;

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        laundry_name.setText(laundry.getname());
        if(laundry.getIsExpress()) express_badge.setVisible(true);

        animationOnHover();
    }

    private void animationOnHover() {
        this.setOnMouseEntered(event -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);
            transition.setToY(-10);
            transition.play();
        });

        this.setOnMouseExited(event -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);
            transition.setToY(0);
            transition.play();
        });
    }
}
