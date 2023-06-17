package siplaundry.controller.cashier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.Session;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import siplaundry.data.SessionData;
import siplaundry.entity.TransactionDashboardEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.entity.UserEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.view.cashier.components.column.DashboardColumn;
import siplaundry.view.cashier.components.column.TransactionColumn;

public class DashboarController {
    @FXML
    private Text greet_name, txt_cust, txt_process, txt_taken;

    @FXML
    private BarChart<String, Number> statistic_chart;

    @FXML
    private VBox dash_table;

    private BorderPane shadowRoot;

    TransactionRepo transRepo = new TransactionRepo();


    private String cust, process, taken;

    public DashboarController(BorderPane shadow){ this.shadowRoot = shadow; }

    @FXML
    void initialize() {
        fillInformation();
        statistic_chart.setStyle("-fx-bar-width: 10px; -fx-bar-gap: 5px;");

        Timeline timeline = new Timeline();
       timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
                statistic_chart.getYAxis().setAnimated(false);
                statistic_chart.getXAxis().setAnimated(false);
               drawChart("7");
           }
       }));
       timeline.play();

       List<TransactionDashboardEntity> trans = transRepo.DashboardTable();
       showTable(trans);
    }

     private void drawChart(String duration) {
        XYChart.Series<String, Number> total = new XYChart.Series<>();
        
        UserEntity data = SessionData.user;
        LinkedHashMap<String, Integer> dataPend = transRepo.chartCount("user_id = " + data.getID() + " AND "  ,"DAYNAME", "COUNT(transaction_id)", "transaction_date", duration, "DAY");
        
        total.setName("Total Transaksi");
        for (Map.Entry<String, Integer> entry : dataPend.entrySet()) {
            String bulanPend = entry.getKey();
            int totalPendapatan = entry.getValue();
            total.getData().add(new XYChart.Data<>(bulanPend, totalPendapatan));
        }

        statistic_chart.getData().clear();
        statistic_chart.getData().addAll(total);
    }

    private void fillInformation(){
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

    public void showTable(List<TransactionDashboardEntity> trans){
        dash_table.getChildren().clear();

        if(trans == null) transRepo.DashboardTable();
        for(TransactionDashboardEntity transEn: trans){
            DashboardColumn column = new DashboardColumn(shadowRoot, transEn);
            dash_table.getChildren().add(column);
        }
    }

}
