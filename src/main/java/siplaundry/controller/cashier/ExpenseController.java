package siplaundry.controller.cashier;

import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.view.cashier.components.ExpenseColumn;

public class ExpenseController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_expense;

    @FXML
    private VBox expense_table;

    @FXML
    private Text total_text;

    @FXML
    private TextField txt_keyword;

    private ExpanseRepo expRepo = new ExpanseRepo();

    public ExpenseController(BorderPane shadow) {this.shadowRoot = shadow;}

    @FXML
    void initialize(){
        List<ExpenseEntity> exp = expRepo.get();

        showTable(exp);
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

    public void showTable(List<ExpenseEntity> exp){
        expense_table.getChildren().clear();

        if(exp == null)exp = expRepo.get();
        for (ExpenseEntity expEn : exp){
            if(expEn.getUser_id().getID().equals(SessionData.user.getID())){
            expense_table.getChildren().add(new ExpenseColumn(shadowRoot, this::showTable, expEn));
            }
        }

        total_text.setText("Menampilkan total " + exp.size() + " data pengeluaran");
    }
}
