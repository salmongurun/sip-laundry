package toast;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class Toast extends AnchorPane {
    @FXML
    private Text message;
    @FXML
    private FontIcon icon;
    private AnchorPane rootNode;
    private double duration = 2;
    public Toast(AnchorPane rootNode) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/toast/toast.fxml"));
        this.rootNode = rootNode;

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    public Toast setDuration(double seconds) {
        this.duration = seconds;
        return this;
    }

    public void show(ToastType type, String messageText, Runnable action) {
        if(type == ToastType.SUCCESS) {
            this.getStyleClass().add("toast-success");
            icon.setIconLiteral("bx-check");
        };

        if(type == ToastType.FAILED) {
            this.getStyleClass().add("toast-failed");
            icon.setIconLiteral("bx-x");
        }

        this.message.setText(messageText);

        AnchorPane.setRightAnchor(this, 10.0);
        AnchorPane.setBottomAnchor(this, 10.0);

        rootNode.getChildren().add(this);

        AnimationFX incomingAnimation = new FadeInRight(this);
        AnimationFX outcomingAnimation = new FadeOut(this);
        outcomingAnimation.setDelay(Duration.millis(this.duration * 1000));

        incomingAnimation.setOnFinished(event -> {
            outcomingAnimation.play();
        });

        outcomingAnimation.setOnFinished(event -> {
            if(action != null) action.run();
            rootNode.getChildren().remove(this);
        });


        incomingAnimation.play();
    }
}
