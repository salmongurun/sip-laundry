package siplaundry.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepotest {
    private static UsersRepo repo = new UsersRepo();
    private static Integer userId;

    @Test
    @Order(1)
    public void testAdd() {
        UserEntity acc = new UserEntity(
                "jok",
                "joki",
                "081238560",
                "coba9jk",
                "jemberj",
                AccountRole.cashier);

        userId = repo.add(acc);
        assertTrue(userId > 0);
    }

    @Test
    @Order(2)
    public void testGet() {
        UserEntity acc = repo.get(userId);
        UserEntity acc2 = repo.get(new HashMap<String, Object>() {
            {
                put("user_id", userId);
            }
        }).get(0);

        assertEquals("joki", acc.getFullname());
        assertEquals(AccountRole.cashier, acc.getRole());
        assertEquals("coba9jk", acc2.getPassword());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        UserEntity acc2 = new UserEntity(
            // "aldea",
            // "2323232",
            // "haikamu",
            // "jember",
            // AccountRole.cashier
        );
        acc2.setRfid("78786754");
        acc2.setUsername("al21");
        acc2.setFullname("aldea");
        acc2.setPhone("3232333333");
        acc2.setPassword("haikamu");
        acc2.setAddress("jember");
        acc2.setRole(AccountRole.cashier);

        assertTrue(repo.add(acc2) > 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test
    @Order(4)
    public void testSearch() {
        UserEntity acc2 = repo.get(new HashMap<String, Object>() {{
                put("user_id", userId);
        }}).get(0);

        assertEquals("coba9jk", acc2.getPassword());
    }

    @Test
    @Order(5)
    public void testUpdate() {
        UserEntity user = repo.get(userId);
        user.setFullname("John Wick");

        repo.Update(user);
        user = repo.get(userId);

        assertNotEquals("John", user.getFullname());
        assertEquals("John Wick", user.getFullname());
    }

    @Test
    @Order(6)
    public void testDelete() {
        assertTrue(repo.delete(userId));
    }
}
