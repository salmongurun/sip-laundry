package siplaundry.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import siplaundry.data.SessionData;
import siplaundry.entity.LaundryDashboardEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.repository.LaundryRepo;
import siplaundry.repository.TransactionRepo;
import siplaundry.view.admin.components.column.DashboardColumn;

public class DashboardController {
    @FXML
    Text greet_name, txt_cust, txt_process, txt_taken;

    @FXML
    private BarChart<String, Number> statistic_chart;

    @FXML
    private ComboBox<String> CB_chart;
    @FXML
    private VBox laundries_container;

    TransactionRepo transRepo = new TransactionRepo();
    private String cust, process, taken;

    private ExpanseRepo expRepo = new ExpanseRepo();

    @FXML
    void initialize() {
       fillInformations();
       showTable(new LaundryRepo().getMostUsed());

       Timeline timeline = new Timeline();
       timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
                statistic_chart.getYAxis().setAnimated(false);
                statistic_chart.getXAxis().setAnimated(false);
               drawChart("5");
           }
       }));
       timeline.play();


        ObservableList<String> items = FXCollections.observableArrayList(
            "3",
            "5",
            "6",
            "12"
        );
        CB_chart.setItems(items);
    }

    private void drawChart(String duration) {
        XYChart.Series<String, Number> pendapatan = new XYChart.Series<>();
        XYChart.Series<String, Number> pengeluaran = new XYChart.Series<>();
        
        LinkedHashMap<String, Integer> dataPend = transRepo.chartCount(null, "MONTHNAME","SUM(amount)", "transaction_date", duration, "MONTH");
        LinkedHashMap<String, Integer> dataPeng = expRepo.chartCount(null, "MONTHNAME","SUM(amount)","expense_date", duration, "MONTH");
        
        pendapatan.setName("Pendapatan");
        for (Map.Entry<String, Integer> entry : dataPend.entrySet()) {
            String bulanPend = entry.getKey();
            int totalPendapatan = entry.getValue();
            pendapatan.getData().add(new XYChart.Data<>(bulanPend, totalPendapatan));
        }

        pengeluaran.setName("Pengeluaran");
        for (Map.Entry<String, Integer> entry : dataPeng.entrySet()) {
            String bulanPeng = entry.getKey();
            int totalPengeluaran = entry.getValue();
            pengeluaran.getData().add(new XYChart.Data<>(bulanPeng, totalPengeluaran));
        }

        statistic_chart.getData().clear();
        statistic_chart.getData().addAll(pendapatan, pengeluaran);
    
    }


    private void fillInformations() {
        greet_name.setText(SessionData.user.getFullname());

        cust = transRepo.DashboardCount("count", "customer_id");
        process = transRepo.DashboardCount("count", "transaction_id", new HashMap<>(){{
            put("status", "process");
        }});
        taken = transRepo.DashboardCount("count", "transaction_id", new HashMap<>(){{
            put("status", "taken");
        }});

        txt_cust.setText(cust);
        txt_process.setText(process);
        txt_taken.setText(taken);
    }

    @FXML
    void chartClicked(){
        statistic_chart.getData().clear();
        statistic_chart.getXAxis().setAnimated(false);
        drawChart(CB_chart.getValue());
    }

    @FXML
    void dashboard(){
        String outputFileName = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            outputFileName = selectedFile.getAbsolutePath();
        } else {
            System.out.println("No file selected.");
        }
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputFileName));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(new Phrase("Category 1"));
            PdfPCell cell2 = new PdfPCell(new Phrase("Subcategory 1"));
            PdfPCell cell3 = new PdfPCell(new Phrase("Category 2"));
            PdfPCell cell4 = new PdfPCell(new Phrase("Subcategory 2"));
            PdfPCell cell5 = new PdfPCell(new Phrase("Subcategory 3"));

            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);

            cell1.setRowspan(2);
            cell3.setRowspan(2);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);

            document.open();
            document.add(new Paragraph("Hello, iText in JavaFX!"));
            document.add(new Paragraph("Hello, coba"));
            document.add(table);

            document.close();

        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public void showTable(List<LaundryDashboardEntity> laundries) {
        laundries_container.getChildren().clear();

        for(LaundryDashboardEntity laundry: laundries) {
            DashboardColumn column = new DashboardColumn(laundry);
            laundries_container.getChildren().add(column);
        }
    }
}
