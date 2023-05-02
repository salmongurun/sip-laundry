package siplaundry.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class AdminViewController {
    @FXML
    private HBox btn_sb_dashboard;

    @FXML
    private HBox btn_sb_accounts;

    @FXML
    private HBox btn_sb_prices;

    @FXML
    private HBox btn_sb_reports;

    @FXML
    private HBox btn_sb_transactions;

    @FXML
    private AnchorPane parent_element;

    @FXML
    void showDashboard(MouseEvent event) {
        removeAllStates();
        btn_sb_dashboard.getStyleClass().add("active");
    }

    @FXML
    void showAccounts(MouseEvent event) {
        removeAllStates();
        btn_sb_accounts.getStyleClass().add("active");

//        parent_element
    }

    @FXML
    void showPrices(MouseEvent event) {
        removeAllStates();
        btn_sb_prices.getStyleClass().add("active");
    }

    @FXML
    void showReports(MouseEvent event) {
        removeAllStates();
        btn_sb_reports.getStyleClass().add("active");
    }

    @FXML
    void showTransactions(MouseEvent event) {
        removeAllStates();
        btn_sb_transactions.getStyleClass().add("active");
    }

    private void removeAllStates() {
        btn_sb_dashboard.getStyleClass().remove("active");
        btn_sb_prices.getStyleClass().remove("active");
        btn_sb_accounts.getStyleClass().remove("active");
        btn_sb_reports.getStyleClass().remove("active");
        btn_sb_transactions.getStyleClass().remove("active");
    }
}
