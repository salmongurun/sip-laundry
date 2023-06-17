package siplaundry.view.admin.components.column;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.cashier.components.modal.ExpenseModal;
import toast.Toast;
import toast.ToastType;

public class ExpenseColumn extends HBox{
     @FXML
    private Text txt_date, txt_name, txt_amount, txt_user, txt_optional;

     @FXML
    private HBox edit_btn, delete_btn;

    @FXML
    private CheckBox bulk_check;

    private ExpenseEntity exp;
    private ExpanseRepo expRepo = new ExpanseRepo();
    private BorderPane shadowRoot;

    private Consumer<List<ExpenseEntity>> refreshTable;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

     public ExpenseColumn(BorderPane shadowRoot, Consumer<List<ExpenseEntity>> refreshTable, ExpenseEntity exp){
        this.exp = exp;
        this.shadowRoot = shadowRoot;
        this.refreshTable = refreshTable;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/expense/column.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize(){
        Toast toast = new Toast((AnchorPane) shadowRoot.getScene().getRoot());
        
        txt_name.setText(exp.getName());
        txt_date.setText(dateFormat.format(exp.getExpanse_date()));
        txt_user.setText(exp.getUser_id().getFullname());
        txt_amount.setText(String.valueOf(exp.getAmount()));
        txt_optional.setText(exp.getOptional());

        edit_btn.setOnMouseClicked(event -> {
            new ExpenseModal(shadowRoot, refreshTable, exp);
        });

        delete_btn.setOnMouseClicked(event -> {
            new ConfirmDialog(shadowRoot, () -> {
                expRepo.delete(exp.getExpanse_id());
                refreshTable.accept(null);
                toast.setDuration(1).show(ToastType.SUCCESS, "Berhasil menghapus data", null);
            });
        });
    }

    public void setBulkAction(Consumer<ExpenseEntity> action) {
        bulk_check.selectedProperty().addListener((ob, ov, nv) -> {
            action.accept(this.exp);
        });
    }

    public void toggleBulk(){ bulk_check.setSelected(!bulk_check.isSelected()); }

}
