package siplaundry.controller.admin;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.ExpenseEntity;
import siplaundry.repository.ReportExpRepository;
import siplaundry.repository.ReportRepository;
import siplaundry.service.ExpenseReportService;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.ReportView;
import siplaundry.view.admin.components.column.ReportExpColumn;
import siplaundry.view.util.EmptyData;

public class ReportExpenseController {
   @FXML
   private VBox report_table;

   @FXML
   private Text total_text, first_date_text, second_date_text;

   @FXML
   private HBox first_date_filter, second_date_filter;

   private BorderPane parentRoot, shadowRoot;
   private ReportExpRepository reportRepo = new ReportExpRepository();
   private PopOver datePopOver = new PopOver();
   private LocalDate firstDate, secondDate;

   public ReportExpenseController(BorderPane parentRoot, BorderPane shadowRoot){
    this.parentRoot = parentRoot;
    this.shadowRoot = shadowRoot;
   }

   @FXML
   void initialize(){
    showTable();
   }

   @FXML
   void exportData(){
    new ExpenseReportService().generateReportPdf(reportRepo.resultExp(firstDate, secondDate));
   }

   @FXML
   void showFirstDateFilter(){
    datePopOver.show(first_date_filter);
    datePopOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    datePopOver.setContentNode(dateFilterElement(firstDate, (value) -> {
        firstDate = value;
        first_date_text.setText(ViewUtil.formatDate(value, "dd/MM/YYYY"));
        showTable();
    }));
   }

   @FXML
   void showSecondDateFilter(){
    datePopOver.show(second_date_filter);
    datePopOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    datePopOver.setContentNode(dateFilterElement(secondDate, (value) -> {
        secondDate = value;
        second_date_text.setText(ViewUtil.formatDate(value, "dd/MM/YYYY"));
        showTable();
    }));
   }

   @FXML
   void showTransactionReport() throws IOException{
        parentRoot.setCenter(new ReportView(parentRoot, shadowRoot));
   }

   void showTable(){
    List<ExpenseEntity> expense = reportRepo.resultExp(firstDate, secondDate);
    report_table.getChildren().clear();

    if(expense.size() < 1){
        report_table.getChildren().add(new EmptyData(null, null));
    }

    for(int i = 0; i < expense.size(); i++){
        ExpenseEntity exp = expense.get(i);
        System.out.println("exp");
        ReportExpColumn column = new ReportExpColumn(shadowRoot, exp);

        if(i % 2 == 1) column.getStyleClass().add("stripped");
        report_table.getChildren().add(column);
    }

    total_text.setText("Menampilkan " + expense.size() + " data laporan");
   }

   private VBox dateFilterElement(LocalDate date, Consumer<LocalDate> callback){
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
