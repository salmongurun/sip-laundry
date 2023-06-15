package siplaundry.service;

import javafx.application.Application;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import siplaundry.view.print.ReceiptPrint;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.embed.swing.SwingFXUtils;

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
//    public static void printReceipt(WritableImage snapshot) {
//        PixelReader reader = snapshot.getPixelReader();
//
//        int width = (int) snapshot.getWidth();
//        int height = (int) snapshot.getHeight();
//        int[] pixels = new int[width * height];
//
//        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
//
//        try {
//            Path tempDirectory = Files.createTempDirectory("snapshot");
//            Path imagePath = tempDirectory.resolve("snapshot.png");
//
//            saveImage(imagePath, pixels, width, height);
//            printImage(imagePath);
//            deleteImage(imagePath, tempDirectory);
//        } catch (IOException e) { e.printStackTrace(); }
//    }
//
//    private static void saveImage(Path imagePath, int[] pixels, int width, int height) throws IOException {
//        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        File outputFile = imagePath.toFile();
//
//        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
//        ImageIO.write(bufferedImage, "png", outputFile);
//    }
//
//    private static void printImage(Path imagePath) throws IOException {
//        FileInputStream imageFile = new FileInputStream(imagePath.toFile());
//        DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
//        Doc doc = new SimpleDoc(imageFile, flavor, null);
//        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, null);
//
//        if(printServices.length > 0) {
//            PrintService printService = printServices[0];
//            DocPrintJob printJob = printService.createPrintJob();
//
//            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
//            attributeSet.add(new Copies(1));
//
//            try {
//                printJob.print(doc, attributeSet);
//            } catch(PrintException e) { e.printStackTrace(); }
//        }
//    }
//
//    private static void deleteImage(Path imagePath, Path tempDirectory) throws IOException {
//        System.out.println(imagePath.toString());
//        System.out.println(tempDirectory.toString());
////        Files.deleteIfExists(imagePath);
////        Files.deleteIfExists(tempDirectory);
//    }
}
