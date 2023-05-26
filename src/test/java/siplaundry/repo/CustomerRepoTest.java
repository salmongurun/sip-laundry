package siplaundry.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerRepoTest {
    private static CustomerRepo repo = new CustomerRepo();
    private static Integer accountId;

    @Test
    @Order(1)
    public void testAdd() {
        CustomerEntity acc = new CustomerEntity(
                "joki",
                "081238560",
                "jember"
                );

        accountId = repo.add(acc);
        assertTrue(accountId > 0);
    }

    @Test
    @Order(2)
    public void testGet() {
        CustomerEntity acc = repo.get(accountId);
        CustomerEntity acc2 = repo.get(new HashMap<String, Object>() {
            {
                put("customer_id", accountId);
            }
        }).get(0);

        assertTrue(repo.add(acc) > 0);
        assertEquals("joki", acc.getname());
        assertEquals("081238560", acc2.getphone());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        CustomerEntity acc = new CustomerEntity(
                "Aldea",
                "33232323",
                "jember"
                );

        assertTrue(repo.add(acc) > 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test @Order(4) //kalau mau nyoba ini jangan disatuin sama delete
    public void testSearch(){
    CustomerEntity acc2 = repo.search(new HashMap<String, Object>() {{
        put("customer_id", accountId);
    }}).get(0);

    assertEquals("081238560", acc2.getphone());
    }

    @Test @Order(5)
    public void testUpdate() {
        CustomerEntity user = repo.get(accountId);
        user.setname("john wick");

        repo.Update(user);
        user = repo.get(accountId);

        assertNotEquals("John", user.getname());
        assertEquals("john wick", user.getname());
    }

    @Test
    @Order(6)
    public void testDelete() {
        assertTrue(repo.delete(accountId));
    }
}
