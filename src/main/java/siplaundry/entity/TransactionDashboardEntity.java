package siplaundry.entity;

public class TransactionDashboardEntity {
    private TransactionEntity transaction;
    private CustomerEntity customer;
    private long diffHours;
    private Integer itemsCount;
    
    public TransactionDashboardEntity(TransactionEntity transaction, CustomerEntity customer, long diffHours,
            Integer itemsCount) {
        this.transaction = transaction;
        this.customer = customer;
        this.diffHours = diffHours;
        this.itemsCount = itemsCount;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public long getDiffHours() {
        return diffHours;
    }

    public void setDiffHours(long diffHours) {
        this.diffHours = diffHours;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }
}
