package siplaundry.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import javafx.fxml.FXML;
import siplaundry.data.Laundryunit;

public class LaundryEntity extends Entity {

    public static String tableName = "laundries";

    private int laundry_id;
    @NotNull(message = "Satuan harus diisi")
    private Laundryunit unit;

    @NotNull(message = "Harga harus diisi")
    private Integer cost;

    @NotBlank(message = "Nama harus diisi")
    private String name;

    @NotNull(message = "Express harus diisi")
    private boolean IsExpress;

    
    public LaundryEntity() {
    }
    
    public LaundryEntity(Laundryunit unit, Integer cost, String name, boolean isExpress) {
        this.unit = unit;
        this.cost = cost;
        this.name = name;
        IsExpress = isExpress;
    }
    

    public int getid() {
        return laundry_id;
    }

    public void setid(int id) {
        this.laundry_id = id;
    }

    public Laundryunit getunit() {
        return this.unit;
    }

    public void setunit(Laundryunit unit) {
        this.unit = unit;
    }

    public int getcost() {
        return this.cost;
    }

    public void setcost(int cost) {
        this.cost = cost;
    }

    public String getname() {
        return this.name;
    }

    public boolean getIsExpress() {
        return IsExpress;
    }

    public void setIsExpress(boolean isExpress) {
        IsExpress = isExpress;

    }

    public void setname(String name) {
        this.name = name;
    }

    

}
