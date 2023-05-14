package siplaundry.controller.admin;

import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.view.admin.components.column.PriceColumn;

public class PriceController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_price;

    @FXML
    private VBox price_table;

    @FXML
    private Text total_Text;

    @FXML
    private TextField txt_keyword;

    private LaundryRepo laundryRepo = new LaundryRepo();

    public PriceController(BorderPane shadow){ this.shadowRoot = shadow; }

    @FXML
    void initialize(){
        List<LaundryEntity> laundry = laundryRepo.get();

        showTable(laundry);

        total_Text.setText("Menampilkan total "+ laundry.size() +" data akun");

    }

    @FXML
    void searchAction(KeyEvent event){
        String keyword = txt_keyword.getText();

        List<LaundryEntity> laundry = laundryRepo.search(new HashMap<>() {{
            put("name", keyword);
            put("unit", keyword);
            put("cost", keyword);
        }});

        showTable(laundry);
    }

    void showTable(List<LaundryEntity> laundry){
        price_table.getChildren().clear();

        if(laundry == null) laundry = laundryRepo.get();
        for(LaundryEntity lndry: laundry){ price_table.getChildren().add(new PriceColumn(shadowRoot, this::showTable, lndry)); }
    }
}
