package siplaundry.view.cashier.transaction;

import animatefx.animation.FadeIn;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import siplaundry.entity.LaundryEntity;
import siplaundry.util.NumberUtil;

import java.io.IOException;
import java.util.function.Consumer;

public class TransactionCard extends AnchorPane {
    @FXML
    private Text laundry_name, laundry_price;
    @FXML
    private HBox express_badge;

    private BorderPane shadowRoot;
    private LaundryEntity laundry;
    private Consumer<LaundryEntity> addAction;

    public TransactionCard(BorderPane shadowRoot, LaundryEntity laundry, Consumer<LaundryEntity> addAction) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/card.fxml"));
        this.laundry = laundry;
        this.addAction = addAction;
        this.shadowRoot = shadowRoot;

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        laundry_name.setText(laundry.getname());
        laundry_price.setText("Rp " + NumberUtil.rupiahFormat(laundry.getcost()) + "/ " + laundry.getunit().toString());

        if(laundry.getIsExpress()) express_badge.setVisible(true);

        this.setOnMouseClicked(event -> {
            if(this.laundry.getIsExpress()) {
                askExpressMode();
                return;
            }

            addAction.accept(this.laundry);
        });

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

    private void askExpressMode() {
        LaundryEntity selectedLaundry = new LaundryEntity();
        selectedLaundry.setid(laundry.getid());
        selectedLaundry.setcost(laundry.getcost());
        selectedLaundry.setunit(laundry.getunit());
        selectedLaundry.setname(laundry.getname());

        new ExpressChoose(
            shadowRoot,
            () -> {
                selectedLaundry.setIsExpress(true);
                selectedLaundry.setcost(selectedLaundry.getcost() + 1000);

                addAction.accept(selectedLaundry);
                return true;
            },
            () -> {
                selectedLaundry.setIsExpress(false);
                addAction.accept(selectedLaundry);
                return false;
            }
        );
    }
}
