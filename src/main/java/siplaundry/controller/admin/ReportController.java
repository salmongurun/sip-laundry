package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.view.admin.components.column.ReportColumn;

import java.util.List;

public class ReportController {
    @FXML
    private VBox report_table;
    @FXML
    private Text total_text;

    private BorderPane shadowRoot;
    private TransactionRepo transRepo = new TransactionRepo();

    public ReportController(BorderPane shadowRoot) {
        this.shadowRoot = shadowRoot;
    }

    @FXML
    void initialize() {
        showTable(transRepo.get());
    }

    @FXML
    void exportData() {

    }

    void showTable(List<TransactionEntity> transactions) {
        report_table.getChildren().clear();

        for(int i = 0; i < transactions.size(); i++) {
            TransactionEntity transaction = transactions.get(i);
            ReportColumn column = new ReportColumn(shadowRoot, transaction);

            if(i % 2 == 1) column.getStyleClass().add("stripped");
            report_table.getChildren().add(column);
        }

        total_text.setText("Menampilkan " + transactions.size() + " data laporan");
    }
}
