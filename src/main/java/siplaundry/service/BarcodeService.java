package siplaundry.service;



// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.MultiFormatWriter;
// import com.google.zxing.WriterException;
// import com.google.zxing.common.BitMatrix;

// import javafx.scene.image.PixelWriter;
// import javafx.scene.image.WritableImage;

public class BarcodeService {

    // // public String setIdBarcode(String date, int id){
    // //     String newId = Integer.toString(id);
    // //     return date + "-" + newId;
    // // }

    // private WritableImage generate(String date, int id, int width, int height)throws WriterException{
    //     String code = date + "-" + Integer.toString(id);
    //     BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.CODE_128, width, height);
    //     WritableImage image = new WritableImage(width, height);
    //     PixelWriter writer = image.getPixelWriter();
    //     for (int x = 0; x < width; x++) {
    //         for (int y = 0; y < height; y++) {
    //             boolean bit = bitMatrix.get(x, y);
    //             writer.setArgb(x, y, bit ? 0x000000 : 0xFFFFFF);
    //         }
    //     }
    //     return image;

    // }

    // private void saveImageToFile(WritableImage image, File file) {
    //     try {
    //         WritableImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
    //         ImageIO.write(bufferedImage, IMAGE_FORMAT, file);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
