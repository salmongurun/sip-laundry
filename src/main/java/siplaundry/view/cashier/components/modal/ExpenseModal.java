package siplaundry.view.cashier.components.modal;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
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
import siplaundry.data.SessionData;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.ValidationUtil;
import toast.Toast;
import toast.ToastType;

public class ExpenseModal {
    @FXML
    private Text modal_title, modal_subtitle;

    @FXML
    private Button close_btn;

    @FXML
    private TextField txt_name, txt_subtotal, txt_qty;

    @FXML
    private TextArea txt_optional;

    @FXML
    private DatePicker txt_date;

    LocalDate localDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ExpanseRepo expRepo = new ExpanseRepo();
    private Node shadowRoot;
    private ExpenseEntity exp;

    private Consumer<List<ExpenseEntity>> refreshTable;
    private Map<String, Node> fields;

    public ExpenseModal(Node shadowRoot, Consumer<List<ExpenseEntity>> refreshTable, ExpenseEntity exp){
        this.shadowRoot = shadowRoot;
        this.exp = exp;
        this.refreshTable = refreshTable;

        initModal();
    }

    @FXML
    public void initialize(){
        txt_date.setValue(localDate.now());
          if(this.exp != null) {
            changeUpdate();
          }
        this.fields = new HashMap<>() {{
            put("name", txt_name);
            put("subtotal", txt_subtotal);
            put("qty", txt_qty);
            put("optional", txt_optional);
        }};

    }

    @FXML
    void closeModal(){
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void saveAction(){
        if(exp != null){
            saveUpdate();
            return;
        }

        expRepo.add(validateExp());

        (new Toast((AnchorPane) shadowRoot.getScene().getRoot()))
            .show(ToastType.SUCCESS, "Berhasil menambahkan akun", null);
        closeModal();
    }

    
    private ExpenseEntity validateExp(){
        localDate = txt_date.getValue();
        Date date = Date.valueOf(localDate);
        ExpenseEntity exp = new ExpenseEntity(
            txt_name.getText(), 
            date, 
            NumberUtil.convertToInteger(txt_qty.getText()), 
            NumberUtil.convertToInteger(txt_subtotal.getText()), 
            txt_optional.getText(), 
            SessionData.user
        );

        Set<ConstraintViolation<ExpenseEntity>> vols = ValidationUtil.validate(exp);
        ValidationUtil.renderErrors(vols, this.fields);

        return exp;        
    }

    private void initModal(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/expense/add.fxml"));
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
            // TODO: handle exception
        }
    }

    private void changeUpdate(){
        modal_title.setText("Edit Data Pengeluaran");
        modal_subtitle.setText("Perbarui Informasi");

        // LocalDate newDate = (exp.getExpanse_date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         String coba = dateFormat.format(exp.getExpanse_date());
        LocalDate newDate = LocalDate.parse(coba);

        txt_date.setValue(newDate);
        txt_name.setText(exp.getName());
        txt_subtotal.setText(String.valueOf(exp.getSubtotal()));
        txt_qty.setText(String.valueOf(exp.getQty()));
        txt_optional.setText(exp.getOptional());

    }

    private void saveUpdate(){
        localDate = txt_date.getValue();
        Date date = Date.valueOf(localDate);

        exp.setName(txt_name.getText());
        exp.setExpanse_date(date);
        exp.setSubtotal(NumberUtil.convertToInteger(txt_subtotal.getText()));
        exp.setQty(NumberUtil.convertToInteger(txt_qty.getText()));
        exp.setOptional(txt_optional.getText());

        expRepo.Update(validateExp());

        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil mengupdate akun", null);
        closeModal();
    }
}
