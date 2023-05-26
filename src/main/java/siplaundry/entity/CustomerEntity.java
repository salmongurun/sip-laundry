package siplaundry.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerEntity extends Entity {
    public final static String tableName = "customers";
    private int cust_id;

    @NotBlank(message = "Nama harus diisi")
    private String name;

    @NotBlank(message = "No telepon harus diisi")
    @Size(max = 12, min = 11, message = "No telepon harus valid")
    private String phone;

    @NotBlank(message = "Alamat harus diisi")
    private String address;

    public CustomerEntity(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    };

    public int getid() {
        return this.cust_id;
    }

    public void setid(int id) {
        this.cust_id = id;
    }

    public String getname() {
        return this.name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getphone() {
        return this.phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
