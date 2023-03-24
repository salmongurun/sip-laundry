package siplaundry.entity;

import jakarta.validation.constraints.NotBlank;

public class VerificationEntity extends Entity {
    public static String tableName = "verifications";

    @NotBlank(message = "harus diisi")
    private UserEntity user_id;

    private String code;

    public VerificationEntity() {
    }

    public VerificationEntity(UserEntity user_id, String code) {
        this.user_id = user_id;
        this.code = code;
    }

    public UserEntity getUserID() {
        return user_id;
    }

    public void setUserID(UserEntity user_id) {
        this.user_id = user_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
