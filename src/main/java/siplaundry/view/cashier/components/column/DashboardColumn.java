package siplaundry.view.cashier.components.column;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.TransactionDashboardEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;

public class DashboardColumn extends HBox{
    @FXML
    private Text txt_name, txt_amount, txt_time;

    private BorderPane shadowRoot;
    private TransactionDashboardEntity transDashboard;
    private TransactionEntity trans;
    private TransactionRepo transRepo = new TransactionRepo();
    Map<String, String> totalItem = new HashMap<>();
    
    Date date;
    

    public DashboardColumn(BorderPane shadowRoot, TransactionDashboardEntity transDashboard){
        this.shadowRoot = shadowRoot;
        this.transDashboard = transDashboard;

         FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/dashboard/column.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try { loader.load(); } 
        catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize(){
        totalItem = transRepo.detailCount();
        date = transDashboard.getTransaction().getpickupDate();
        Instant instant = date.toInstant();
        LocalDate pickup_date = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now(); 

        Duration duration = Duration.between(now, pickup_date);
        long hour = duration.toHours();

        txt_name.setText(trans.getCustomerID().getname() + " - " + totalItem.get(transDashboard.getTransaction()) + " items");
        txt_time.setText(Long.toString(hour) + " jam lagi");
        txt_amount.setText(String.valueOf(trans.getamount()));

    }
}
