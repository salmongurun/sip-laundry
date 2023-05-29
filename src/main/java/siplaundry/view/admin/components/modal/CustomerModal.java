package siplaundry.view.admin.components.modal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.util.ValidationUtil;
import toast.Toast;
import toast.ToastType;

public class CustomerModal {
    @FXML
    private Text modal_tittle, modal_subtitle;

    @FXML
    private Button close_btn;

    @FXML
    private TextField txt_fullname, txt_phone;
    
    @FXML
    private TextArea txt_address;

    private CustomerRepo custRepo = new CustomerRepo();

    private Node shadowRoot;

    private CustomerEntity cust;

    private Consumer<List<CustomerEntity>> refreshTable;
    private Map<String, Node> fields;

    public CustomerModal(Node shadowRoot, Consumer<List<CustomerEntity>> refreshTable, CustomerEntity cust){
        this.shadowRoot = shadowRoot;
        this.cust = cust;
        this.refreshTable = refreshTable;

        initModal();
    }

    @FXML
    public void initialize(){
        this.fields = new HashMap<>(){{
            put("name", txt_fullname);
            put("phone", txt_phone);
            put("address", txt_address);
        }};

        if(this.cust != null) changeUpdate();

        this.fields = new HashMap<>() {{
            put("name" , txt_fullname);
            put("phone", txt_phone);
            put("address", txt_address);
        }};
    }

    @FXML
    void closeModal(){
        Window window = close_btn.getScene().getWindow();
        window.fireEvent((new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST)));
    }

    @FXML
    void saveAction(){
        if(cust != null){
           saveUpdate();
           return;
        }

        custRepo.add(validateAccount());
        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
                .show(ToastType.SUCCESS, "Berhasil menambahkan customer", null);
        closeModal();
    }

    @FXML
    CustomerEntity validateAccount(){
        CustomerEntity cust = new CustomerEntity(
            txt_fullname.getText(), 
            txt_phone.getText(),
            txt_address.getText()
        );

        Set<ConstraintViolation<CustomerEntity>> vols = ValidationUtil.validate(cust);
        ValidationUtil.renderErrors(vols, this.fields);

        return cust;
    }

    private void initModal(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/customer/add.fxml"));
        loader.setController(this);

        try {
            Parent root = loader.load();
            Stage modalStage = new Stage();
            Scene modalScene = new Scene(root);

            modalScene.setFill(Color.TRANSPARENT);
            modalStage.setScene(modalScene);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.setResizable(false);

            modalStage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
                refreshTable.accept(null);
            });

            shadowRoot.setVisible(true);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeUpdate(){
        modal_tittle.setText("Edit Data Customer");
        modal_subtitle.setText("Perbarui Informasi");

        txt_fullname.setText(cust.getname());
        txt_phone.setText(cust.getphone());  
        txt_address.setText(cust.getAddress());      
    }

    public void saveUpdate(){
        cust.setname(txt_fullname.getText());
        cust.setphone((txt_phone.getText()));
        cust.setAddress(txt_address.getText());

        validateAccount();
        custRepo.Update(cust);

        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil mengupdate customer", null);
        closeModal();
    }
}
