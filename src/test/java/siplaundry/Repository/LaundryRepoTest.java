package siplaundry.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import siplaundry.Entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;
import siplaundry.repository.Repository;

public class LaundryRepoTest {
    private static LaundryRepo repo = new LaundryRepo();
    private static Integer accountId;

    @Test @Order(1)
    public void testAdd() {
        LaundryEntity acc = new LaundryEntity(
            "joki",
            323,
            "coba9jk"
        );
        accountId = repo.add(acc);
        assertTrue(accountId > 0);
    }

    @Test @Order(2)
    public void testGet() {
        LaundryEntity acc = repo.get(accountId);
        LaundryEntity acc2 = repo.get(new HashMap<String, Object>() {{
            put("laundry_id", accountId);
        }}).get(0);

        assertEquals("joki", acc.getunit());
        assertEquals("coba9jk", acc2.getname());
    }

    @Test @Order(3)
    public void testGetAll() {
        LaundryEntity acc = new LaundryEntity(
            "Aldea",
            3323,
            "aldea"
        );
 
        assertTrue(repo.add(acc) > 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test @Order(4)
    public void testSearch(){
        LaundryEntity acc2 = repo.get(new HashMap<String, Object>() {{
            put("laundry_id", accountId);
        }}).get(0);

        assertEquals("coba9jk", acc2.getname());
    }

    // @Test @Order(4)   //-->belom jalan
    // public void testUpdate() {
    //     LaundryEntity user = repo.get(accountId);
    //     user.setunit("Joh");

    //     repo.Update(user);
    //     user = repo.get(accountId);

    //     assertNotEquals("joki", user.getunit());
    //     assertEquals("Joh", user.getunit());
    // }

    @Test @Order(5)
    public void testDelete() {
        assertTrue(repo.delete(accountId));
    }
}
