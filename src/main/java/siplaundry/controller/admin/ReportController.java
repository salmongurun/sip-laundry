package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.view.admin.components.column.ReportColumn;

import java.util.List;

public class ReportController {
    @FXML
    private VBox report_table;

    private BorderPane shadowRoot;
    private TransactionRepo transRepo = new TransactionRepo();

    public ReportController(BorderPane shadowRoot) {
        this.shadowRoot = shadowRoot;
    }

    @FXML
    void initialize() {
        showTable(transRepo.get());
    }

    void showTable(List<TransactionEntity> transactions) {
        report_table.getChildren().clear();

        for(TransactionEntity transaction: transactions) {
            report_table.getChildren().add(new ReportColumn(shadowRoot, transaction));
        }
    }
}
