package siplaundry.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.Window;
import siplaundry.data.SessionData;
import siplaundry.view.EditProfileView;
import siplaundry.view.admin.*;
import siplaundry.view.admin.components.modal.logOutConfirm;
import siplaundry.view.auth.LoginView;

import java.io.IOException;

public class AdminViewController {
    @FXML
    private HBox btn_sb_dashboard, btn_sb_accounts, btn_sb_cust, btn_sb_prices, btn_sb_expense;

    @FXML
    private Pane btn_edit;

    @FXML
    private HBox btn_sb_reports, btn_sb_transaction, btn_logOut;

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
    
        btn_logOut.setOnMouseClicked(event -> {
            new logOutConfirm(shadow, () -> {
                LogOut();
            });
        });
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

        parent_root.setCenter(new ReportView(shadow));
    }

    @FXML
    void showTransactions(MouseEvent event) throws IOException {
        removeAllStates();
        btn_sb_transaction.getStyleClass().add("active");

        parent_root.setCenter(new TransactionView(shadow));
    }

    @FXML
    void showEditProfile()throws IOException{
         parent_root.setCenter(new EditProfileView(shadow));
    }

    @FXML
    void showExpense() throws IOException{
        removeAllStates();
        btn_sb_expense.getStyleClass().add("active");

        parent_root.setCenter(new ExpenseView(shadow));
    }

    private void removeAllStates() {
        btn_sb_dashboard.getStyleClass().remove("active");
        btn_sb_prices.getStyleClass().remove("active");
        btn_sb_accounts.getStyleClass().remove("active");
        btn_sb_cust.getStyleClass().remove("active");
        btn_sb_reports.getStyleClass().remove("active");
        btn_sb_transaction.getStyleClass().remove("active");
        btn_sb_expense.getStyleClass().remove("active");
    }

    
    void LogOut(){
        try {
            (new LoginView()).start(stage);
            Window window = btn_logOut.getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        } catch (IOException e) {e.printStackTrace(); }
    }

}
