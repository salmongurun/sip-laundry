package siplaundry.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import siplaundry.data.AccountRole;

public class UserEntity extends Entity {
    public static String tableName = "users";

    private Integer user_id;

    private String rfid;

    @NotBlank(message = "Username harap diisi")
    private String username;

    @NotBlank(message = "Nama lengkap harus diisi")
    private String fullname;

    @NotBlank(message = "No telepon harus diisi")
    @Size(min = 11, max = 15, message = "Masukkan no telepon yang valid")
    private String phone;

    @NotBlank(message = "Password harus diisi")
    @Size(min = 8, message = "Password minimal 8 huruf")
    private String password;

    @NotBlank(message = "Alamat harus diisi")
    private String address;

    @NotNull(message = "Peran harus diisi")
    private AccountRole role;

    public UserEntity() {
    };

    public UserEntity(String username, String fullname, String phone, String password, String address, AccountRole role) {
        this.username = username;
        this.fullname = fullname;
        this.phone = formatPhone(phone);
        this.password = password;
        this.address = address;
        this.role = role;
    };

    public String getRfid() {
        return rfid;
    };

    public void setRfid(String rfid) {
        this.rfid = rfid;
    };

    public Integer getID() {
        return user_id;
    };

    public void setID(int id) {
        this.user_id = id;
    };



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = formatPhone(phone);
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AccountRole getRole() {
        return this.role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    private String formatPhone(String phone) {
        if(phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "62");
        }

        if(phone.startsWith("+")) {
            phone = phone.replaceFirst("/+", "");
        }

        return phone;
    }
}
