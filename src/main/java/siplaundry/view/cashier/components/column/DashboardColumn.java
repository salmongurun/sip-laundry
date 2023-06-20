package siplaundry.view.cashier.components.column;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.TransactionDashboardEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.repository.TransactionRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;
import siplaundry.view.invoice.InvoiceDetailView;

public class DashboardColumn extends HBox{
    @FXML
    private Text detail_name, detail_total, detail_hours;

    private BorderPane parentRoot, shadowRoot;
    private TransactionDashboardEntity transDashboard;
    private TransactionRepo transRepo = new TransactionRepo();
    Map<String, String> totalItem = new HashMap<>();
    
    Date date;
    

    public DashboardColumn(BorderPane parentRoot, BorderPane shadowRoot, TransactionDashboardEntity transDashboard){
        this.shadowRoot = shadowRoot;
        this.parentRoot = parentRoot;
        this.transDashboard = transDashboard;

         FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/dashboard/column.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try { loader.load(); } 
        catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize(){
        TransactionEntity trans = transDashboard.getTransaction();
        totalItem = transRepo.detailCount();
        date = transDashboard.getTransaction().getpickupDate();

        detail_name.setText("Transaksi#" + trans.getid() + " - " + transDashboard.getItemsCount() + " item");
        detail_total.setText("Total: Rp " + NumberUtil.rupiahFormat(trans.getamount()));
        detail_hours.setText(ViewUtil.diffHuman(transDashboard.getDiffHours()));
    }

    @FXML
    void showDetail() {
        parentRoot.setCenter(new InvoiceDetailView(parentRoot, shadowRoot, transDashboard.getTransaction()));
    }
}
