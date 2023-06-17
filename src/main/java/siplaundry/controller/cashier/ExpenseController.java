package siplaundry.controller.cashier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.data.SortingOrder;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.cashier.components.ExpenseColumn;
import siplaundry.view.cashier.components.modal.ExpenseModal;
import siplaundry.view.util.EmptyData;
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
    private ComboBox<String> CB_column;

    @FXML
    private FontIcon sort_icon;

    private SortingOrder sortOrder = SortingOrder.DESC;
    private ExpanseRepo expRepo = new ExpanseRepo();
    private Set<ExpenseEntity> bulkItems = new HashSet<>();
    private ArrayList<ExpenseColumn> accColumns = new ArrayList<>();

    public ExpenseController(BorderPane shadow) {this.shadowRoot = shadow;}

    @FXML
    void initialize(){
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
    void showAddExpense(){
        new ExpenseModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void searchAction(KeyEvent event){
        List<ExpenseEntity> exp = expRepo.search(this.searchableValues());
        showTable(exp);
    }

    private HashMap<String, Object> searchableValues(){
        String keyword = txt_keyword.getText();
        return new HashMap<>(){{
            put("name", keyword);
            put("expense_date", keyword);
            put("subtotal", keyword);
            put("qty", keyword);
            put("amount", keyword);
            put("optional", keyword);
        }};
    }

    @FXML
    void sortAction(){
        String column = "name";

        this.sortOrder = ViewUtil.switchOrderIcon(this.sortOrder, this.sort_icon);

        if(CB_column.getValue() != null){
            if(CB_column.getValue().equals("Tanggal Pengeluaran")) column = " expense_date";
            if(CB_column.getValue().equals("Total Harga")) column = " amount";
        }
       

        List<ExpenseEntity> exp = expRepo.search(this.searchableValues(), column, this.sortOrder);
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
        if(exp.size() < 1){
            expense_table.getChildren().add(new EmptyData(this::showAddExpense, txt_keyword.getText()));
        }
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
