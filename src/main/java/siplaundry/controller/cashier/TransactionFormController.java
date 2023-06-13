package siplaundry.controller.cashier;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.TransactionUtil;
import siplaundry.view.cashier.transaction.CartItem;
import siplaundry.view.cashier.transaction.CustomerPopover;
import siplaundry.view.cashier.transaction.TransactionCard;
import siplaundry.view.print.ReceiptPrint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TransactionFormController {
    @FXML
    private ScrollPane card_scroll, items_scroll;
    @FXML
    private Text transaction_title, cart_grandtotal;
    @FXML
    private HBox choose_customer;
    private BorderPane parent_root;
    private LaundryRepo laundryRepo = new LaundryRepo();
    private ArrayList<TransactionDetailEntity> details = new ArrayList<>();
    private Set<LaundryEntity> laundries = new HashSet<>();
    public TransactionFormController(BorderPane parentRoot) {
        this.parent_root = parentRoot;
    }

    @FXML
    void initialize() {
        showCardItems((ArrayList<LaundryEntity>) laundryRepo.get());

        transaction_title.setOnMouseClicked(event -> {
            new ReceiptPrint();
        });
    }

    @FXML
    void showCustomerList() {
        PopOver popover = new PopOver();

        popover.setContentNode(new CustomerPopover());
        popover.show(choose_customer);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    }

    void showCardItems(ArrayList<LaundryEntity> laundries) {
        GridPane cardGrid = new GridPane();

        cardGrid.setVgap(20);
        cardGrid.setHgap(20);
        cardGrid.setPadding(new Insets(10, 20, 10, 20));

        for(int i = 0; i < laundries.size(); i++) {
            int row = i / 3;
            int column = i % 3;
            TransactionCard card = new TransactionCard(
                laundries.get(i),
                this::addDetailTransaction
            );

            cardGrid.add(card, column, row);
        }

        card_scroll.setContent(cardGrid);
    }

    void showCartItems(ArrayList<TransactionDetailEntity> details) {
        VBox cartBox = new VBox();
        int grandTotal = 0;

        cartBox.setSpacing(2.5);

        for(TransactionDetailEntity detail: details) {
            grandTotal += detail.getSubtotal();

            cartBox.getChildren().add(new CartItem(
                detail,
                this::addDetailQuantity,
                this::redDetailQuantity
            ));
        }

        cart_grandtotal.setText("Rp " + NumberUtil.rupiahFormat(grandTotal));
        items_scroll.setContent(cartBox);
    }

    void addDetailTransaction(LaundryEntity laundry) {
        int exist = TransactionUtil.getExistLaundry(laundry, this.details);
        TransactionDetailEntity detail = new TransactionDetailEntity();

        if(exist >= 0) {
            addDetailQuantity(this.details.get(exist));
            return;
        }

        detail.setLaundry(laundry);
        detail.setQty(1);
        detail.setSubtotal(laundry.getcost());

        this.details.add(detail);
        this.laundries.add(laundry);
        showCartItems(this.details);
    }

    void addDetailQuantity(TransactionDetailEntity detail) {
        int index = this.details.indexOf(detail);
        TransactionDetailEntity addedDetail = this.details.get(index);

        this.details.get(index).setQty(addedDetail.getQty() + 1);
        this.details.get(index).setSubtotal(detail.getLaundry().getcost() * addedDetail.getQty());
        showCartItems(this.details);
    }

    void redDetailQuantity(TransactionDetailEntity detail) {
        int index = this.details.indexOf(detail);

        System.out.println(index);
        TransactionDetailEntity addedDetail = this.details.get(index);

        if(addedDetail.getQty() <= 1) {
            this.details.remove(index);
            showCartItems(this.details);
            return;
        }

        this.details.get(index).setQty(addedDetail.getQty() - 1);
        this.details.get(index).setSubtotal(detail.getLaundry().getcost() * addedDetail.getQty());
        showCartItems(this.details);
    }
}
