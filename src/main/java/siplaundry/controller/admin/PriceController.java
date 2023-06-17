package siplaundry.controller.admin;

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
import siplaundry.data.SortingOrder;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.util.ViewUtil;
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
    private FontIcon sort_icon;
    @FXML
    private ComboBox<String> CB_column;

    private LaundryRepo laundryRepo = new LaundryRepo();

    private Set<LaundryEntity> bulkItems = new HashSet<>();
    private ArrayList<PriceColumn> accColumns = new ArrayList<>();
    private SortingOrder sortOrder = SortingOrder.DESC;
    private String keyword;

    public PriceController(BorderPane shadow){ this.shadowRoot = shadow; }

    @FXML
    void initialize(){
        ObservableList<String> column = FXCollections.observableArrayList(
            "Jenis Cucian",
            "Unit",
            "Harga"
        );
        CB_column.setItems(column);

        List<LaundryEntity> laundry = laundryRepo.get();

        showTable(laundry);

    }

    @FXML
    void searchAction(KeyEvent event){
        List<LaundryEntity> laundry = laundryRepo.search(this.searchableValues());
        showTable(laundry);
    }

    private HashMap<String, Object> searchableValues(){
        keyword = txt_keyword.getText();
        if(keyword.equals("reguler")){ 
            keyword = "0"; 
            return new HashMap<>(){{
                put("IsExpress", keyword);
            }};
        }
        if(keyword.equals("express")){ 
            keyword = "1";
            return new HashMap<>(){{
                put("IsExpress", keyword);
            }}; 
        }

        return new HashMap<>(){{
            put("name", keyword);
            put("unit", keyword);
            put("cost", keyword);
            put("IsExpress", keyword);
        }};
    }

    @FXML
    void showAddPrice() {
        new ServiceModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void sortAction(){
        String column = "name";

        this.sortOrder = ViewUtil.switchOrderIcon(this.sortOrder, this.sort_icon);

        if(CB_column.getValue() != null){
            if(CB_column.getValue().equals("Unit")) column = " unit";
            if(CB_column.getValue().equals("Harga")) column = " cost";
        }

        List<LaundryEntity> laundry = laundryRepo.search(this.searchableValues(), column, this.sortOrder);
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
         total_Text.setText("Menampilkan total "+ laundry.size() +" data akun");
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
