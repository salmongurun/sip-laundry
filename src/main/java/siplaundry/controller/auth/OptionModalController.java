package siplaundry.controller.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.data.SessionData;
import siplaundry.repository.OptionRepo;
import toast.Toast;
import toast.ToastType;

public class OptionModalController {
    @FXML
    private TextField txt_name, txt_cost, txt_phone, txt_address;

    @FXML
    private Button close_btn;

    private Node shadowRoot;
    OptionRepo opt = new OptionRepo();

    public OptionModalController(Node shadowRoot){
        this.shadowRoot = shadowRoot;
        initModal();
    }

    @FXML
    void initialize(){

        txt_name.setText(opt.get("name").getValue());
        txt_address.setText(opt.get("address").getValue());
        txt_phone.setText(opt.get("phone").getValue());
        txt_cost.setText(opt.get("costExpress").getValue());

    }

    private void initModal(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/option.fxml"));
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
            });

            shadowRoot.setVisible(true);
            modalStage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void saveAction(){
        Map<String, String> fields = new HashMap<>(){{
            put("name", txt_name.getText());
            put("address", txt_address.getText());
            put("phone", txt_phone.getText());
            put("costExpress", txt_cost.getText());
        }};

        for(Map.Entry<String, String> option: fields.entrySet()){
            opt.update(option.getKey(), option.getValue());
        }

         (new Toast((AnchorPane) shadowRoot.getScene().getRoot()))
            .show(ToastType.SUCCESS, "Berhasil mengganti detail toko", null);
        closeModal();
    }

    @FXML
    void closeModal(){
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
