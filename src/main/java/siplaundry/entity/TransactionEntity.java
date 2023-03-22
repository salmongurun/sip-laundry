package siplaundry.entity;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;

public class TransactionEntity extends Entity {
    public static String tableName = "users";

    private int transaction_id;

    private java.util.Date transaction_Date;

    private java.util.Date pickup_date;

    private LaundryStatus status;

    @NotBlank(message = "jenis pembayaran harus diisi")
    private PaymentStatus payment_status;
    
    private int amount;

    private UserEntity userID;
    private CustomerEntity customerID;

    // public transactionsEntity(java.util.Date transaksi_date, java.util.Date
    // picup_date, LaundryStatus finish, PaymentStatus paid, int i, UserEntity user,
    // CustomerEntity customer){}

    public TransactionEntity() {
    }

    public TransactionEntity(Date transactionDate, Date pickup_Date, LaundryStatus status,
            PaymentStatus payment_status, int amount, UserEntity user_id, CustomerEntity customer_id) {
        this.transaction_Date = transactionDate;
        this.pickup_date = pickup_Date;
        this.status = status;
        this.payment_status = payment_status;
        this.amount = amount;
        this.userID = user_id;
        this.customerID = customer_id;
    }

    public int getid() {
        return transaction_id;
    }

    public void setid(int id) {
        this.transaction_id = id;
    }

    public java.util.Date gettransactionDate() {
        return transaction_Date;
    }

    public void settransactionDate(Date transactionDate) {
        this.transaction_Date = transactionDate;
    }

    public java.util.Date getpickupDate() {
        return pickup_date;
    }

    public void setpickupDate(Date pickupDate) {
        this.pickup_date = pickupDate;
    }

    public LaundryStatus getstatus() {
        return this.status;
    }

    public void setstatus(LaundryStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return this.payment_status;
    }

    public void setPaymentSatatus(PaymentStatus paymentStatus) {
        this.payment_status = paymentStatus;
    }

    public int getamount() {
        return this.amount;
    }

    public void setamount(int amount) {
        this.amount = amount;
    }

    public UserEntity getUserID() {
        return userID;
    }

    public void setUserID(UserEntity userID) {
        this.userID = userID;
    }

    public CustomerEntity getCustomerID() {
        return customerID;
    }

    public void setCustomerID(CustomerEntity customerID) {
        this.customerID = customerID;
    }
}
