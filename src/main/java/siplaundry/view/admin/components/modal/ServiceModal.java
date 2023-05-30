package siplaundry.view.admin.components.modal;

import jakarta.validation.ConstraintViolation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import siplaundry.data.Laundryunit;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.ValidationUtil;
import toast.Toast;
import toast.ToastType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ServiceModal {
    @FXML
    private ComboBox<Laundryunit> input_unit;
    @FXML
    private TextField txt_name, txt_cost;
    @FXML
    private CheckBox input_express;
    @FXML
    private Button close_btn;
    @FXML
    private Text modal_title;
    private Node shadowRoot;
    private Consumer<List<LaundryEntity>> refreshTable;
    private LaundryEntity laundry;

    private Map<String, Node> fields;
    private LaundryRepo laundryRepo = new LaundryRepo();

    public ServiceModal(Node shadowRoot, Consumer<List<LaundryEntity>> refreshTable, LaundryEntity laundry) {
        this.shadowRoot = shadowRoot;
        this.laundry = laundry;
        this.refreshTable = refreshTable;

        initModal();
    }

    @FXML
    void initialize() {
        input_unit.setItems(FXCollections.observableArrayList(Laundryunit.values()));
        this.fields = new HashMap<>() {{
            put("name", txt_name);
            put("cost", txt_cost);
            put("unit", input_unit);
        }};

        if(laundry != null) {
            modal_title.setText("Edit Layanan");
            changeUpdate();
        }
    }

    @FXML
    void saveAction() {
        if(laundry != null) {
            saveUpdate();
            return;
        }

        laundryRepo.add(validateLaundry());
        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil menambahkan layanan", null);
        closeModal();
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private LaundryEntity validateLaundry() {
        LaundryEntity laundry = new LaundryEntity(
            input_unit.getValue(),
            NumberUtil.convertToInteger(txt_cost.getText()),
            txt_name.getText(),
            input_express.isSelected()
        );

        Set<ConstraintViolation<LaundryEntity>> vols = ValidationUtil.validate(laundry);
        ValidationUtil.renderErrors(vols, this.fields);

        return laundry;
    }

    private void initModal() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/price/add.fxml"));
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
        } catch(IOException e) { e.printStackTrace(); }
    }

    private void saveUpdate() {
        laundry.setname(txt_name.getText());
        laundry.setcost(NumberUtil.convertToInteger(txt_cost.getText()));
        laundry.setunit(input_unit.getValue());
        laundry.setIsExpress(input_express.isSelected());

        validateLaundry();
        laundryRepo.Update(laundry);

        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil mengupdate layanan", null);
        closeModal();
    }

    private void changeUpdate() {
        txt_name.setText(laundry.getname());
        txt_cost.setText(String.valueOf(laundry.getcost()));
        input_unit.setValue(laundry.getunit());
        input_express.setSelected(laundry.getIsExpress());
    }
}
