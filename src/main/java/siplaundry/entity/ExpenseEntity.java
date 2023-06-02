package siplaundry.entity;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExpenseEntity extends Entity {
    public static String tableName = "expanse";

    private Integer expanse_id;

    @NotBlank(message = "Harap isikan katagori pengeluaran")
    private String name;

    private Date expanse_date;

    @NotNull(message = "jumlah produk harap diisi")
    private Integer qty;

    @NotNull(message = "Harap masukkan harga tiap produk")
    private Integer subtotal;

    private Integer amount;

    @NotBlank(message = "Harap diisi")
    private String optional;

    private UserEntity user_id;

    public ExpenseEntity(String name, Date expanse_date, Integer qty, Integer subtotal, String optional, UserEntity user_id) {
        this.name = name;
        this.expanse_date = expanse_date;
        this.qty = qty;
        this.subtotal = subtotal;
        this.optional = optional;
        this.user_id = user_id;
    }
    public ExpenseEntity(String name, Date expanse_date, int qty, int subtotal, int amount, String optional, UserEntity user_id) {
        this.name = name;
        this.expanse_date = expanse_date;
        this.qty = qty;
        this.subtotal = subtotal;
        this.amount = amount;
        this.optional = optional;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }
    
    public int getExpanse_id() {
        return expanse_id;
    }

    public void setid(int expanse_id) {
        this.expanse_id = expanse_id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Date getExpanse_date() {
        return expanse_date;
    }

    public void setExpanse_date(Date expanse_date) {
        this.expanse_date = expanse_date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UserEntity getUser_id() {
        return user_id;
    }

    public void setUser_id(UserEntity user_id) {
        this.user_id = user_id;
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

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }


    
    
}
