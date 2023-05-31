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
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.admin.components.modal.CustomerModal;
import toast.Toast;
import toast.ToastType;

public class CustomerColumn extends HBox{
    @FXML
    private Text txt_name, txt_phone, txt_address; 
    
    @FXML
    private CheckBox bulk_check;

    @FXML
    private HBox edit_btn, delete_btn;

    private CustomerEntity cust;
    private CustomerRepo custRepo = new CustomerRepo();
    private BorderPane shadowRoot;

    private Consumer<List<CustomerEntity>> refreshTable;

    public CustomerColumn(BorderPane shadowRoot, Consumer<List<CustomerEntity>> refreshTable, CustomerEntity cust){
        this.cust = cust;
        this.shadowRoot = shadowRoot;
        this.refreshTable = refreshTable;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/customer/column.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {e.printStackTrace();} 
    }

    @FXML
    void initialize(){
        txt_name.setText(cust.getname());
        txt_phone.setText(cust.getphone());
        txt_address.setText(cust.getAddress());

        edit_btn.setOnMouseClicked(event -> {
            new CustomerModal(shadowRoot, refreshTable, cust);
        });

        delete_btn.setOnMouseClicked(event -> {
            new ConfirmDialog(shadowRoot, () -> {
                custRepo.delete(cust.getid());

                new Toast((AnchorPane) shadowRoot.getScene().getRoot())
                    .show(ToastType.SUCCESS, "Berhasil menghapus customer", null);
                refreshTable.accept(null);
            });
        });

    }

    public void setBulkAction(Consumer<CustomerEntity> action) {
        bulk_check.selectedProperty().addListener((ob, ov, nv) -> {
            action.accept(this.cust);
        });
    }

    public void toggleBulk(){
        bulk_check.setSelected(!bulk_check.isSelected());
    }
}
