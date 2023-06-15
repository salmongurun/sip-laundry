package siplaundry.view.print;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import siplaundry.service.PrinterService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReceiptPrint {
    @FXML
    private Text store_name;

    @FXML
    private AnchorPane print_body;

    public ReceiptPrint() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/print/receipt.fxml"));

        try {
            loader.setController(this);

            Parent root = loader.load();
            Stage stage =  new Stage();
            Scene scene = new Scene(root);

            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);

            stage.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        SnapshotParameters parameters = new SnapshotParameters();
        WritableImage snapshot = new WritableImage(300, 1000);
        print_body.snapshot(parameters, snapshot);

        PrinterService.printReceipt(snapshot);
    }
}
