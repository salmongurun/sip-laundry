package siplaundry.entity;

public class TransactionDetailEntity extends Entity {
    public static String tableName = "transaction_details";

    private TransactionEntity transaction;
    private LaundryEntity laundry;
    private int qty;
    private int subtotal;

    public TransactionDetailEntity() {
    }

    public TransactionDetailEntity(TransactionEntity transaction, LaundryEntity laundry, int qty, int subtotal) {
        this.transaction = transaction;
        this.laundry = laundry;
        this.qty = qty;
        this.subtotal = subtotal;
    }

    public TransactionDetailEntity(TransactionEntity transaction, LaundryEntity laundry, int qty) {
        this.transaction = transaction;
        this.laundry = laundry;
        this.qty = qty;
        this.subtotal = laundry.getcost() * qty;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public LaundryEntity getLaundry() {
        return laundry;
    }

    public void setLaundry(LaundryEntity laundry) {
        this.laundry = laundry;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

}
