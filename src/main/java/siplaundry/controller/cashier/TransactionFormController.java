package siplaundry.controller.cashier;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.TransactionUtil;
import siplaundry.view.cashier.transaction.CartItem;
import siplaundry.view.cashier.transaction.CustomerPopover;
import siplaundry.view.cashier.transaction.PaymentModal;
import siplaundry.view.cashier.transaction.TransactionCard;
import siplaundry.view.print.ReceiptPrint;
import siplaundry.view.util.WarningModal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TransactionFormController {
    @FXML
    private ScrollPane card_scroll, items_scroll;
    @FXML
    private Text transaction_title, cart_grandtotal, customer_name, customer_phone;
    @FXML
    private HBox choose_customer;
    private BorderPane parent_root, shadow_root;
    private LaundryRepo laundryRepo = new LaundryRepo();
    private ArrayList<TransactionDetailEntity> details = new ArrayList<>();
    private Set<LaundryEntity> laundries = new HashSet<>();
    private PopOver custPopover = new PopOver();
    private CustomerEntity customer;
    private boolean expressMode = false;

    public TransactionFormController(BorderPane parentRoot, BorderPane shadow_root) {
        this.parent_root = parentRoot;
        this.shadow_root = shadow_root;
    }

    @FXML
    void initialize() {
        showCardItems((ArrayList<LaundryEntity>) laundryRepo.get());
    }

    @FXML
    void showCustomerList() {
        custPopover.setContentNode(new CustomerPopover(this::setCustomer, shadow_root));
        custPopover.show(choose_customer);
        custPopover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    }

    @FXML
    void showPaymentModal() {
        if(customer == null) {
            new WarningModal(shadow_root, "Anda belum memilih customer!");
            return;
        }

        if(details.isEmpty()) {
            new WarningModal(shadow_root, "Anda belum menambahkan item");
            return;
        }

        new PaymentModal(
            shadow_root,
            this.details,
            this.customer
        );
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
                shadow_root,
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

        if(laundry.getIsExpress() && !expressMode) {
            details.clear();
            expressMode = true;
        }

        if(!laundry.getIsExpress() && expressMode) {
            details.clear();
            expressMode = false;
        }

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

    void setCustomer(CustomerEntity customer) {
        customer_name.setText(customer.getname());
        customer_phone.setText(customer.getphone());

        this.customer = customer;
        custPopover.hide();
    }
}
