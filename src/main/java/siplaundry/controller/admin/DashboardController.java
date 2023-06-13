package siplaundry.controller.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import siplaundry.data.SessionData;
import siplaundry.repository.ExpanseRepo;
import siplaundry.repository.TransactionRepo;

public class DashboardController {
    @FXML
    Text greet_name, txt_cust, txt_process, txt_taken;

    @FXML
    private BarChart<String, Number> statistic_chart;

    @FXML
    private ComboBox<String> CB_chart;

    TransactionRepo transRepo = new TransactionRepo();
    private String cust, process, taken;

    private ExpanseRepo expRepo = new ExpanseRepo();

    @FXML
    void initialize() {
       fillInformations();

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
}
