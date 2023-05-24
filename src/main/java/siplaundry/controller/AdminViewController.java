package siplaundry.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.Window;
import siplaundry.data.SessionData;
import siplaundry.view.admin.*;
import siplaundry.view.auth.LoginView;

import java.io.IOException;

public class AdminViewController {
    @FXML
    private HBox btn_sb_dashboard;

    @FXML
    private HBox btn_sb_accounts;

    @FXML
    private HBox btn_sb_cust;

    @FXML
    private HBox btn_sb_prices;

    @FXML
    private HBox btn_sb_reports;

    @FXML
    private HBox btn_sb_transactions;

    @FXML
    private HBox btn_logOut;

    @FXML
    private BorderPane parent_root;

    @FXML
    private Text topbar_name;

    @FXML
    private BorderPane shadow;

    Stage stage = new Stage();

    @FXML
    void initialize() throws IOException {
        parent_root.setCenter(new DashboardView());
        topbar_name.setText(SessionData.user.getFullname());
        shadow.setVisible(false);
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

        parent_root.setCenter(new AccountView(shadow));
    }

    @FXML
    void showCustomer(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_cust.getStyleClass().add("active");

        parent_root.setCenter(new CustomerView(shadow));
    }

    @FXML
    void showPrices(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_prices.getStyleClass().add("active");

        parent_root.setCenter(new PriceView(shadow));
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
        btn_sb_cust.getStyleClass().remove("active");
        btn_sb_reports.getStyleClass().remove("active");
//        btn_sb_transactions.getStyleClass().remove("active");
    }

    @FXML
    void LogOutAction(MouseEvent event) throws IOException{
        (new LoginView()).start(stage);
        Window window = btn_logOut.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
