package siplaundry.data;

public enum AccountRole {
    admin,
    cashier,
    ;

    @Override
    public String toString() {
        if(this == admin) return "Admin";
        return "Kasir";
    }
}
