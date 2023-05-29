package siplaundry.view.cashier.components;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;

public class ExpenseColumn extends HBox {
    @FXML
    private Text txt_date, txt_name, txt_subtotal, txt_qty, txt_amount, txt_optional;

    @FXML
    private HBox edit_btn, delete_btn;

    private ExpenseEntity exp;
    private ExpanseRepo expRepo;
    private BorderPane shadowRoot;

    private Consumer<List<ExpenseEntity>> refreshTable;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd//MM/YYYY");

    public ExpenseColumn(BorderPane shadowRoot, Consumer<List<ExpenseEntity>> refreshTable, ExpenseEntity exp){
        this.exp = exp;
        this.shadowRoot = shadowRoot;
        this.refreshTable = refreshTable;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/expense/column.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize(){
        txt_name.setText(exp.getName());
        txt_date.setText(dateFormat.format(exp.getExpanse_date()));
        txt_subtotal.setText(Integer.toString(exp.getSubtotal()));
        txt_qty.setText(Integer.toString(exp.getQty()));
        txt_amount.setText(Integer.toString(exp.getAmount()));
        txt_optional.setText(exp.getOptional());

        delete_btn.setOnMouseClicked(event -> {
            new ConfirmDialog(shadowRoot, () -> {
                expRepo.delete(exp.getExpanse_id());
                refreshTable.accept(null);
            });
        });
        
    }
}
