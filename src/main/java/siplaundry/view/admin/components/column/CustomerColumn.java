package siplaundry.view.admin.components.column;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;

public class CustomerColumn extends HBox{
    @FXML
    private Text txt_name, txt_phone;

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

        delete_btn.setOnMouseClicked(event -> {
            new ConfirmDialog(shadowRoot, () -> {
                custRepo.delete(cust.getid());
                refreshTable.accept(null);
            });
        });

    }
}
