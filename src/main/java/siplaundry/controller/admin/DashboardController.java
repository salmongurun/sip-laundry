package siplaundry.controller.admin;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.repository.TransactionRepo;

public class DashboardController {
    @FXML
    Text greet_name, txt_cust, txt_process, txt_taken;

    @FXML
    private BarChart<String, Number> statistic_chart;

    TransactionRepo transRepo = new TransactionRepo();
    private String cust, process, taken;

    @FXML
    void initialize() {
//        fillInformations();
        drawChart();
    }

    private void drawChart() {
        XYChart.Series<String, Number> pendapatan = new XYChart.Series<>();
        XYChart.Series<String, Number> pengeluaran = new XYChart.Series<>();

        pendapatan.setName("Pendapatan");
        pendapatan.getData().add(new XYChart.Data<>("Jan", 20000));
        pendapatan.getData().add(new XYChart.Data<>("Feb", 30000));
        pendapatan.getData().add(new XYChart.Data<>("Mar", 20000));
        pendapatan.getData().add(new XYChart.Data<>("Apr", 40000));
        pendapatan.getData().add(new XYChart.Data<>("May", 10000));

        pengeluaran.setName("Pengeluaran");
        pengeluaran.getData().add(new XYChart.Data<>("Jan", 5000));
        pengeluaran.getData().add(new XYChart.Data<>("Feb", 15000));
        pengeluaran.getData().add(new XYChart.Data<>("Mar", 5000));
        pengeluaran.getData().add(new XYChart.Data<>("Apr", 20000));
        pengeluaran.getData().add(new XYChart.Data<>("May", 2500));

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
}
