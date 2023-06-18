package siplaundry.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

public class PrinterService {
    public static void printReceipt(BufferedImage image) {
        try {
            File tempFile = File.createTempFile("snapshot", ".png");
            ImageIO.write(image, "png", tempFile);

            print(tempFile);
            System.out.println(tempFile.toString());
        }catch(IOException e) { e.printStackTrace(); }
    }

    private static void print(File imagePath) throws IOException {
        FileInputStream imageFile = new FileInputStream(imagePath.toString());
        DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
        Doc doc = new SimpleDoc(imageFile, flavor, null);
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, null);

        if(printServices.length > 0) {
            PrintService printService = printServices[0];
            DocPrintJob printJob = printService.createPrintJob();

            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            attributeSet.add(new Copies(1));

            try {
                printJob.print(doc, attributeSet);
            } catch(PrintException e) { e.printStackTrace(); }
        }
    }
}
