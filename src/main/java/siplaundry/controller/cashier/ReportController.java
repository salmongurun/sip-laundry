package siplaundry.controller.cashier;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.LaundryStatus;
import siplaundry.data.SessionData;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.ReportRepository;
import siplaundry.util.ViewUtil;
import siplaundry.view.cashier.components.column.ReportColumn;
import siplaundry.view.util.EmptyData;

public class ReportController {
     @FXML
    private VBox report_table;
    @FXML
    private Text total_text, first_date_text, second_date_text;
    @FXML
    private HBox status_filter, first_date_filter, second_date_filter;

    private BorderPane shadowRoot;
    private ReportRepository reportRepo = new ReportRepository();
    private PopOver statusPopover = new PopOver();
    private PopOver datePopover = new PopOver();
    private Set<LaundryStatus> statusFilters = new HashSet<>();
    private LocalDate firstDate, secondDate;

     public ReportController(BorderPane shadowRoot) {
        this.shadowRoot = shadowRoot;
    }

    @FXML
    void initialize() {
        for(LaundryStatus status: LaundryStatus.values()) {
            statusFilters.add(status);
        }

        showTable();
    }

    @FXML
    void exportData() {

    }

    @FXML
    void showStatusFilter() {
        statusPopover.show(status_filter);
        statusPopover.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
        statusPopover.setContentNode(statusFilterElement());
    }

    @FXML
    void showFirstDateFilter() {
        datePopover.show(first_date_filter);
        datePopover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        datePopover.setContentNode(dateFilterElement(firstDate, (value) -> {
            firstDate = value;
            first_date_text.setText(ViewUtil.formatDate(value, "dd/MM/YYYY"));
            showTable();
        }));
    }

    @FXML
    void showSecondDateFilter() {
        datePopover.show(second_date_filter);
        datePopover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        datePopover.setContentNode(dateFilterElement(secondDate, (value) -> {
            secondDate = value;
            second_date_text.setText(ViewUtil.formatDate(value, "dd/MM/YYYY"));
            showTable();
        }));
    }

     void showTable() {
        List<TransactionEntity> transactions = reportRepo.result(statusFilters, firstDate, secondDate);
        report_table.getChildren().clear();

        if(transactions.size() < 1){
            report_table.getChildren().add(new EmptyData(null, String.valueOf(statusFilters.size())));
        }

        for(int i = 0; i < transactions.size(); i++) {
            TransactionEntity transaction = transactions.get(i);
            // if(transaction.getUserID() = SessionData.user.getID()){continue;}
            ReportColumn column = new ReportColumn(shadowRoot, transaction);

            if(i % 2 == 1) column.getStyleClass().add("stripped");
            report_table.getChildren().add(column);
        }

        total_text.setText("Menampilkan " + transactions.size() + " data laporan");
    }

    private VBox statusFilterElement() {
        VBox filterContainer = new VBox();
        filterContainer.getStyleClass().add("filter-container");

        for(LaundryStatus status: LaundryStatus.values()) {
            CheckBox filterCheck = new CheckBox(ViewUtil.toStatusString(status));

            filterContainer.getChildren().add(filterCheck);
            filterCheck.setSelected(statusFilters.contains(status));
            filterCheck.getStyleClass().add("filter-item");

            filterCheck.selectedProperty().addListener(event -> {
                boolean added = statusFilters.contains(status) ?
                        statusFilters.remove(status) :
                        statusFilters.add(status);
                showTable();
            });
        }

        return filterContainer;
    }

    private VBox dateFilterElement(LocalDate date, Consumer<LocalDate> callback) {
        VBox filterContainer = new VBox();
        DatePicker datePicker = new DatePicker();

        datePicker.setValue(date);
        datePicker.valueProperty().addListener((ov, oldVal, newVal) -> {
            callback.accept(newVal);
        });

        filterContainer.getStyleClass().add("filter-container");
        filterContainer.getChildren().add(datePicker);

        return filterContainer;
    }
}
