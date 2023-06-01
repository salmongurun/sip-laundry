package siplaundry.view.admin.components.column;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.data.Laundryunit;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.admin.components.modal.ServiceModal;
import toast.Toast;
import toast.ToastType;

public class PriceColumn extends HBox {
    @FXML
    private Text txt_name, txt_unit, txt_cost, txt_IsExpress;

   @FXML
   private HBox edit_btn, delete_btn, unit_background, express_background;

   @FXML
   private CheckBox bulk_check;

   private LaundryEntity laundry;
   private LaundryRepo laundRepo = new LaundryRepo();
   private BorderPane shadowRoot;

   private String express;

   private Consumer<List<LaundryEntity>> refreshTable;

   public PriceColumn(BorderPane shadowRoot, Consumer<List<LaundryEntity>> refreshTable, LaundryEntity laundry){
        this.laundry = laundry;
        this.shadowRoot = shadowRoot;
        this.refreshTable = refreshTable;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/price/column.fxml"));
        
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {}

   }

   @FXML
   void initialize(){
        setUnitColor();
        setExpressColor();

        if(!laundry.getIsExpress()){ express = "Reguler"; }
        else{ express = "Express"; }

        txt_name.setText(laundry.getname());
        txt_unit.setText(laundry.getunit().toString());
        txt_cost.setText(Integer.toString(laundry.getcost()));
        txt_IsExpress.setText(express);

        edit_btn.setOnMouseClicked(event -> {
            new ServiceModal(shadowRoot, refreshTable, laundry);
        });

        delete_btn.setOnMouseClicked(event -> {
            new ConfirmDialog(shadowRoot, () -> {
                laundRepo.delete(laundry.getid());
                new Toast((AnchorPane) shadowRoot.getScene().getRoot())
                    .show(ToastType.SUCCESS, "Berhasil menghapus layanan", null);
                refreshTable.accept(null);
            });
        });
   }

   public void setBulkAction(Consumer<LaundryEntity> action) {
        bulk_check.selectedProperty().addListener((ob, ov, nv) -> {
            action.accept(this.laundry);
        });
    }

    public void toggleBulk(){
        bulk_check.setSelected(!bulk_check.isSelected());
    }

   void setUnitColor(){
        if(laundry.getunit() == Laundryunit.kilogram){
            txt_unit.setStyle("-fx-fill: #6A9A98;");
            unit_background.setStyle(unit_background.getStyle() + "-fx-background-color: #f0f4f4;");
        }else if(laundry.getunit() == Laundryunit.pcs){
            txt_unit.setStyle("-fx-fill: #feb74d;");
            unit_background.setStyle(unit_background.getStyle() + "-fx-background-color: #fff4e5;");
        }
   }

   void setExpressColor(){
    if(!laundry.getIsExpress()){
        txt_IsExpress.setStyle("-fx-fill: #6A9A98;");
        express_background.setStyle(express_background.getStyle() + "-fx-background-color: #f0f4f4;");
    }
   }
}
