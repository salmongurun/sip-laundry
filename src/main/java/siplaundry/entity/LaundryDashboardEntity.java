package siplaundry.entity;

public class LaundryDashboardEntity extends Entity {
    public static String tableName = "laundries";

    private LaundryEntity laundry;
    private int usage;

    public LaundryDashboardEntity(LaundryEntity laundry, int usage) {
        this.laundry = laundry;
        this.usage = usage;
    }

    public LaundryEntity getLaundry() {
        return laundry;
    }

    public void setLaundry(LaundryEntity laundry) {
        this.laundry = laundry;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }
}
