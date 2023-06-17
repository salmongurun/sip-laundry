package siplaundry.util;

import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;

import java.util.ArrayList;

public class TransactionUtil {
    public static int getExistLaundry(LaundryEntity laundry, ArrayList<TransactionDetailEntity> details) {
        int launIndex = -1;

        for(int i = 0; i < details.size(); i++) {
            if (details.get(i).getLaundry().getid() == laundry.getid()) {
                launIndex = i;
                break;
            }
        }

        return launIndex;
    }
}
