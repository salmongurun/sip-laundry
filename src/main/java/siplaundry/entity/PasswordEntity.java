package siplaundry.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import siplaundry.util.validators.SameValue;

@SameValue(propertyPath = "mantap")
public class PasswordEntity {
    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 8, message = "Password minimal 8 karakter")
    private String password;

    @NotBlank(message = "Konfirmasi password tidak boleh kosong")
    private String confirm;
    public PasswordEntity(String password, String confirm) {
        this.password = password;
        this.confirm = confirm;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm() {
        return confirm;
    }
}
