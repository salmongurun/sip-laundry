package siplaundry.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import siplaundry.data.AccountRole;
import siplaundry.entity.ExpenseEntity;
import siplaundry.entity.UserEntity;
import siplaundry.repository.ExpanseRepo;
import siplaundry.repository.UsersRepo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpenseRepoTest {
    private static UserEntity user;
    private static UsersRepo accRepo = new UsersRepo();
    private static ExpanseRepo expRepo = new ExpanseRepo();
    private static Integer userId, expId;

    @BeforeAll
    public static void init() {
        userId = accRepo.add(new UserEntity(
             "tailor23", "Tailor", "tailor@test.com", "tailor", "tailor123", AccountRole.admin));

        user = accRepo.get(userId);
    }

    @AfterAll
    public static void tearDown() {
        accRepo.delete(userId);
    }

    @Test
    @Order(1)
    public void testAdd() {
        ExpenseEntity exp = new ExpenseEntity(
            "sasa", 
            new Date(), 
            2000,
            4, 
            "beli di toko",
            user
        );

        expId = expRepo.add(exp);
        assertTrue(expId > 0);
    }

    @Test
    @Order(2)
    public void testGet() {
        ExpenseEntity exp1 = expRepo.get(expId);
        ExpenseEntity exp2 = expRepo.get(new HashMap<String, Object>() {
          {
            put("user_id", user.getID());
          }  
        }).get(0);

        assertEquals("sasa", exp1.getName());
        assertEquals("sasa", exp2.getName());

    }

    @Test
    @Order(3)
    public void testGetAll() {
        ExpenseEntity exp = new ExpenseEntity(
            "haiii", 
            new Date(), 
            2000, 
            5,
            "beli ditoko",
            user
        );

        assertTrue(expRepo.add(exp) > 0);
        assertTrue(expRepo.get().size() > 1);
    }

    @Test
    @Order(4)
    public void testSearch() {
        ExpenseEntity exp = expRepo.search(new HashMap<String, Object>(){
            {
                put("user_id", user.getID());
            }
        }).get(0);

        assertEquals("sasa", exp.getName());
    }

    @Test
    @Order(5)
    public void testSearchUser() {
        ExpenseEntity exp = expRepo.searchByUser(user, new HashMap<String, Object>(){
            {
                put("user_id", user.getID());
            }
        }).get(0);

        assertEquals("sasa", exp.getName());
    }

    @Test
    @Order(6)
    public void testUpdate() {
        ExpenseEntity exp = expRepo.get(expId);
        exp.setName("terbaru");

        assertTrue(expRepo.Update(exp));
        exp = expRepo.get(expId);

        assertNotEquals("sasa", exp.getName());
        assertEquals("terbaru", exp.getName());
    }

    @Test
    @Order(7)
    public void testDelete() {
        assertTrue(expRepo.delete(expId));
    }

}
