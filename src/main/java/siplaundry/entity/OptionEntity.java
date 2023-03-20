package siplaundry.entity;

import jakarta.validation.constraints.NotBlank;

public class OptionEntity extends Entity {

    public static String tableName = "option";

    @NotBlank(message = "Harus diisi")
    private String key;

    @NotBlank
    private String value;

    public OptionEntity() {
    }

    public OptionEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getTableName() {
        return tableName;
    }

    public static void setTableName(String tableName) {
        OptionEntity.tableName = tableName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
