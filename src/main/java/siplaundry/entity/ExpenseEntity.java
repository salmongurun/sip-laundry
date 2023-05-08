package siplaundry.entity;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

public class ExpenseEntity extends Entity {
    public static String tableName = "expanse";

    private int expanse_id;

    @NotBlank
    private String name;

    private Date expanse_date;

    @NotBlank
    private int amount;

    private UserEntity user_id;

    public ExpenseEntity(String name, Date expanse_date, int amount, UserEntity user_id) {
        this.name = name;
        this.expanse_date = expanse_date;
        this.amount = amount;
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


    
    
}
