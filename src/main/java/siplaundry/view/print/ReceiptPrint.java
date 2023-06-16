package siplaundry.view.print;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.service.PrinterService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class ReceiptPrint {
    @FXML
    private Text store_name, cashier_name, customer_name;
    @FXML
    private AnchorPane print_body;
    @FXML
    private ImageView barcode_image;
    @FXML
    private VBox detail_container;

    private List<TransactionDetailEntity> details;
    private TransactionEntity transaction;

    public ReceiptPrint(TransactionEntity transaction, List<TransactionDetailEntity> details) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/print/receipt.fxml"));

        this.details = details;
        this.transaction = transaction;

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
        Image barcodeImage = generateBarcodeImage("120032023");

        barcode_image.setImage(barcodeImage);
        customer_name.setText(transaction.getCustomerID().getname());
        cashier_name.setText(transaction.getUserID().getFullname());

        for(TransactionDetailEntity detail: this.details) {
            this.detail_container.getChildren().add(
                new ReceiptItem(detail)
            );
        }

        printReceipt();
    }

    private void printReceipt() {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setTransform(javafx.scene.transform.Scale.scale(1.8, 1.8));

        WritableImage snapshot = print_body.snapshot(parameters, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

        PrinterService.printReceipt(bufferedImage);
    }

    private Image generateBarcodeImage(String barcodeText) {
        Code128Bean barcodeGenerator = new Code128Bean();

        final int dpi = 200;

        // Set the desired barcode height and density
        barcodeGenerator.setModuleWidth(0.6);
        barcodeGenerator.doQuietZone(false);

        BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
        barcodeGenerator.generateBarcode(canvasProvider, barcodeText);

        BufferedImage bufferedImage = canvasProvider.getBufferedImage();
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
