package siplaundry.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import siplaundry.data.Laundryunit;
import siplaundry.entity.LaundryEntity;
import siplaundry.repository.LaundryRepo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LaundryRepoTest {
    private static LaundryRepo repo = new LaundryRepo();
    private static Integer accountId;

    @Test
    @Order(1)
    public void testAdd() {
        LaundryEntity acc = new LaundryEntity(
                Laundryunit.meter,
                323,
                "coba9jk",
                true
                );
        accountId = repo.add(acc);
        assertTrue(accountId > 0);
    }

    @Test
    @Order(2)
    public void testGet() {
        LaundryEntity acc = repo.get(accountId);
        LaundryEntity acc2 = repo.get(new HashMap<String, Object>() {
            {
                put("laundry_id", accountId);
            }
        }).get(0);

        assertEquals(323, acc.getcost());
        assertEquals("coba9jk", acc2.getname());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        LaundryEntity acc = new LaundryEntity(
                Laundryunit.kilogram,
                3323,
                "aldea",
                false
                );

        assertTrue(repo.add(acc) > 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test
    @Order(4)
    public void testSearch() {
        LaundryEntity acc2 = repo.search(new HashMap<String, Object>() {
            {
                put("laundry_id", accountId);
            }
        }).get(0);

        assertEquals("coba9jk", acc2.getname());
    }

    @Test @Order(4) //-->belom jalan
    public void testUpdate() {
    LaundryEntity user = repo.get(accountId);
    user.setcost(23333);

    repo.Update(user);
    user = repo.get(accountId);

    assertEquals(23333, user.getcost());
    }

    @Test
    @Order(5)
    public void testDelete() {
        assertTrue(repo.delete(accountId));
    }
}
