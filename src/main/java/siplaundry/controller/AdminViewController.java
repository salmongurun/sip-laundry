package siplaundry.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import siplaundry.view.admin.*;

import java.io.IOException;

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
    private BorderPane parent_root;

    @FXML
    void initialize() throws IOException {
        parent_root.setCenter(new DashboardView());
    }

    @FXML
    void showDashboard(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_dashboard.getStyleClass().add("active");

        parent_root.setCenter(new DashboardView());
    }

    @FXML
    void showAccounts(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_accounts.getStyleClass().add("active");

        parent_root.setCenter(new AccountView());
    }

    @FXML
    void showPrices(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_prices.getStyleClass().add("active");

        parent_root.setCenter(new PriceView());
    }

    @FXML
    void showReports(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_reports.getStyleClass().add("active");

        parent_root.setCenter(new ReportView());
    }

    @FXML
    void showTransactions(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_transactions.getStyleClass().add("active");

        parent_root.setCenter(new TransactionView());
    }

    private void removeAllStates() {
        btn_sb_dashboard.getStyleClass().remove("active");
        btn_sb_prices.getStyleClass().remove("active");
        btn_sb_accounts.getStyleClass().remove("active");
        btn_sb_reports.getStyleClass().remove("active");
        btn_sb_transactions.getStyleClass().remove("active");
    }
}
