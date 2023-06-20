package siplaundry.view.cashier.components.column;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;

public class ReportExpColumn extends HBox{
    @FXML
    private Text expense_name, txt_date, txt_cost, txt_qty, txt_amount, txt_optional;

    private ExpenseEntity exp;
    private ExpanseRepo expRepo = new ExpanseRepo();
    private BorderPane shadowRoot;

    public ReportExpColumn(BorderPane shadowRoot, ExpenseEntity exp){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/report/ExpColumn.fxml"));

        this.shadowRoot = shadowRoot;
        this.exp = exp;

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

     @FXML
    void initialize(){
        expense_name.setText("Pengeluaran#" + exp.getExpanse_id());
        txt_date.setText(ViewUtil.formatDate(exp.getExpanse_date(), "dd/MM/YYYY"));
        txt_cost.setText("Rp " + NumberUtil.rupiahFormat(exp.getSubtotal()));
        txt_qty.setText(String.valueOf(exp.getQty()));
        txt_amount.setText("Rp " + NumberUtil.rupiahFormat(exp.getAmount()));
        txt_optional.setText(exp.getOptional());
    }
}
