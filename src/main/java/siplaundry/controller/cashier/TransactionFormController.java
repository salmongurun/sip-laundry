package siplaundry.controller.cashier;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.service.PrinterService;
import siplaundry.view.cashier.components.CartItem;
import siplaundry.view.cashier.components.TransactionCard;
import siplaundry.view.print.ReceiptPrint;

import java.util.ArrayList;

public class TransactionFormController {
    @FXML
    private ScrollPane card_scroll, items_scroll;

    @FXML
    private Text transaction_title;
    private BorderPane parent_root;
    private LaundryRepo laundryRepo = new LaundryRepo();
    public TransactionFormController(BorderPane parentRoot) {
        this.parent_root = parentRoot;
    }

    @FXML
    void initialize() {
        showCardItems((ArrayList<LaundryEntity>) laundryRepo.get());
        showCartItems(new ArrayList<>() {{
            add(new TransactionDetailEntity());
            add(new TransactionDetailEntity());
            add(new TransactionDetailEntity());
            add(new TransactionDetailEntity());
            add(new TransactionDetailEntity());
            add(new TransactionDetailEntity());
        }});

        transaction_title.setOnMouseClicked(event -> {
            new ReceiptPrint();
        });
    }

    void showCardItems(ArrayList<LaundryEntity> laundries) {
        GridPane cardGrid = new GridPane();

        cardGrid.setVgap(20);
        cardGrid.setHgap(20);
        cardGrid.setPadding(new Insets(10, 20, 10, 20));

        for(int i = 0; i < laundries.size(); i++) {
            int row = i / 3;
            int column = i % 3;

            cardGrid.add(new TransactionCard(laundries.get(i)), column, row);
        }

        card_scroll.setContent(cardGrid);
    }

    void showCartItems(ArrayList<TransactionDetailEntity> details) {
        VBox cartBox = new VBox();
        cartBox.setSpacing(2.5);

        for(TransactionDetailEntity detail: details) {
            cartBox.getChildren().add(new CartItem(detail));
        }

        items_scroll.setContent(cartBox);
    }
}
