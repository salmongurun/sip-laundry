package siplaundry.view.admin.components.column;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.CustomerEntity;

public class CustomerColumn extends HBox{
    @FXML
    private Text txt_name, txt_phone;

    @FXML
    private HBox edit_btn, delete_btn;

    private CustomerEntity cust;

    private BorderPane shadowRoot;

    public CustomerColumn(BorderPane shadowRoot, CustomerEntity cust){
        this.cust = cust;
        this.shadowRoot = shadowRoot;

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


    }
}
