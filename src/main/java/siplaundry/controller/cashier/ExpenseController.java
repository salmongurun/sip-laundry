package siplaundry.controller.cashier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.cashier.components.ExpenseColumn;
import siplaundry.view.cashier.modal.ExpenseModal;
import toast.Toast;
import toast.ToastType;

public class ExpenseController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_expense, btn_bulk_delete;

    @FXML
    private VBox expense_table;

    @FXML
    private Text total_text;

    @FXML
    private TextField txt_keyword;

    @FXML
    private ComboBox<String> CB_column, CB_sortBy;

    private ExpanseRepo expRepo = new ExpanseRepo();
    private Set<ExpenseEntity> bulkItems = new HashSet<>();
    private ArrayList<ExpenseColumn> accColumns = new ArrayList<>();

    public ExpenseController(BorderPane shadow) {this.shadowRoot = shadow;}

    @FXML
    void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList(
            "A-Z",
            "Z-A"
        );
        CB_sortBy.setItems(items);

        ObservableList<String> column = FXCollections.observableArrayList(
            "Katagori Pengeluaran",
            "Tanggal Pengeluaran",
            "Total Harga"
        );
        CB_column.setItems(column);

        List<ExpenseEntity> exp = expRepo.get();

        showTable(exp);
    }

    @FXML
    void showAddExpense(MouseEvent event){
        new ExpenseModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void searchAction(KeyEvent event){
        String keyword = txt_keyword.getText();

        List<ExpenseEntity> exp = expRepo.search(new HashMap<>(){{
            put("name", keyword);
            put("expense_date", keyword);
            put("subtotal", keyword);
            put("qty", keyword);
            put("amount", keyword);
            put("optional", keyword);
        }});
        showTable(exp);

    }

    @FXML
    void SortAction(){
        String SortBy = " DESC";
        String column = " name";

        if(CB_sortBy.getValue().equals("A-Z")) SortBy = " ASC";

        if(CB_column.getValue().equals("Tanggal Pengeluaran")) column = " expense_date";
        if(CB_column.getValue().equals("Total Harga")) column = " amount";

        List<ExpenseEntity> exp = expRepo.sortBy(column, SortBy);
        showTable(exp);

    }

    @FXML
    void selectBulkAll(){
        for(ExpenseColumn column: accColumns){
            column.toggleBulk();
        }
    }

    @FXML
    void bulkDelete(){
        new ConfirmDialog(shadowRoot, () -> {
            for(ExpenseEntity exp: this.bulkItems){
                expRepo.delete(exp.getExpanse_id());
            }

            new Toast((AnchorPane) btn_bulk_delete.getScene().getRoot())
                .show(ToastType.SUCCESS, "Berhasil melakukan hapus semua", null);
             showTable(expRepo.get());
        });
    }

    public void showTable(List<ExpenseEntity> exp){
        expense_table.getChildren().clear();

        if(exp == null)exp = expRepo.get();
        for (ExpenseEntity expEn : exp){
            if(expEn.getUser_id().getID().equals(SessionData.user.getID())){
                ExpenseColumn column = new ExpenseColumn(shadowRoot, this::showTable, expEn);
                column.setBulkAction(this::toggleBulkItem);

                expense_table.getChildren().add(column);
                accColumns.add(column);
            }
        }

        total_text.setText("Menampilkan total " + exp.size() + " data pengeluaran");
    }

    protected void toggleBulkItem(ExpenseEntity exp){
        if(this.bulkItems.contains(exp)){
            this.bulkItems.remove(exp);
        } else{
            this.bulkItems.add(exp);
        }

        btn_bulk_delete.setDisable(this.bulkItems.size() < 1);
    }

}
