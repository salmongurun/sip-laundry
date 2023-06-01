package siplaundry.controller.admin;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.view.admin.components.column.PriceColumn;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.admin.components.modal.ServiceModal;
import siplaundry.view.util.EmptyData;
import toast.Toast;
import toast.ToastType;

public class PriceController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_price, btn_bulk_delete;

    @FXML
    private VBox price_table;

    @FXML
    private Text total_Text;

    @FXML
    private TextField txt_keyword;

    @FXML
    private ComboBox<String> CB_sortBy;
    @FXML
    private ComboBox<String> CB_column;

    private LaundryRepo laundryRepo = new LaundryRepo();

    private Set<LaundryEntity> bulkItems = new HashSet<>();
    private ArrayList<PriceColumn> accColumns = new ArrayList<>();

    public PriceController(BorderPane shadow){ this.shadowRoot = shadow; }

    @FXML
    void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList(
            "A-Z",
            "Z-A"
        );
        CB_sortBy.setItems(items);

        ObservableList<String> column = FXCollections.observableArrayList(
            "Jenis Cucian",
            "Unit"
        );
        CB_column.setItems(column);

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

    @FXML
    void showAddPrice() {
        new ServiceModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void SortAction(){
        String SortBy = " DESC";
        String column = " name";
        
        if(CB_sortBy.getValue().equals("A-Z")) SortBy = " ASC";
        if(CB_column.getValue().equals("Unit")) column = " unit";

        List<LaundryEntity> laundry = laundryRepo.sortBy(column, SortBy);
        showTable(laundry);
     }

     @FXML
     void selectBulkAll(){
        for(PriceColumn column: accColumns){
            column.toggleBulk();
        }
     }

     @FXML
     void bulkDelete(){
        new ConfirmDialog(shadowRoot, () -> {
            for(LaundryEntity laundry: this.bulkItems){
                laundryRepo.delete(laundry.getid());
            }

            new Toast((AnchorPane) btn_bulk_delete.getScene().getRoot())
                    .show(ToastType.SUCCESS, "Berhasil melakukan hapus semua", null);
            showTable(laundryRepo.get());
        });
     }

    void showTable(List<LaundryEntity> laundry){
        price_table.getChildren().clear();

        if(laundry == null) laundry = laundryRepo.get();
        if(laundry.size() < 1){
            price_table.getChildren().add(new EmptyData(this::showAddPrice, txt_keyword.getText()));
        }

        for(LaundryEntity lndry: laundry){ 
            // price_table.getChildren().add(new PriceColumn(shadowRoot, this::showTable, laundry));
            PriceColumn column = new PriceColumn(shadowRoot, this::showTable, lndry);
            column.setBulkAction(this::toggleBulkItem);

            price_table.getChildren().add(column);
            accColumns.add(column);
         }
    }

    protected void toggleBulkItem(LaundryEntity laundry){
        if(this.bulkItems.contains(laundry)){
            this.bulkItems.remove(laundry);
        } else {
            this.bulkItems.add(laundry);
        }

        btn_bulk_delete.setDisable(this.bulkItems.size() < 1);
    }
}
