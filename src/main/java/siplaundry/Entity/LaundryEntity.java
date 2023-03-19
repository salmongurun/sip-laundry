package siplaundry.Entity;

import jakarta.validation.constraints.NotBlank;

public class LaundryEntity extends Entity{

    public static String tableName = "laundries";

    private int laundry_id;

    @NotBlank(message = "Harus diisi")
    private String unit;

    @NotBlank(message = "cost harus diisi")
    private Integer cost;

    @NotBlank(message = "Nama harus diisi")
    private String name;

    public LaundryEntity(){}

    public LaundryEntity(String unit, Integer cost, String nama){
        this.unit = unit;
        this.cost = cost;
        this.name = nama;
    }
    

    public int getid(){
        return laundry_id;
    }

    public void setid(int id){
        this.laundry_id = id;
    }

    public String getunit(){
        return this.unit;
    }

    public void setunit(String unit){
        this.unit = unit;
    }

    public int getcost(){
        return this.cost;
    }

    public void setcost(int cost){
        this.cost = cost;
    }

    public String getname(){
        return this.name;
    }

    public void setname(String name){
        this.name = name;
    }
    
}
