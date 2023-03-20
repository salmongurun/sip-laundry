package siplaundry.repository;

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
import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.UserEntity;
import siplaundry.entity.TransactionEntity;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionRepoTest {
    private static UserEntity user;
    private static CustomerEntity customer;
    private static TransactionRepo repo = new TransactionRepo();
    private static UsersRepo accRepo = new UsersRepo();
    private static CustomerRepo custRepo = new CustomerRepo();
    private static Integer userId, customerId, transactionId;

    @BeforeAll
    public static void init() {
        userId = accRepo.add(new UserEntity(
                "Tailor", "tailor@test.com", "tailor", "tailor123", AccountRole.admin));

        customerId = custRepo.add(new CustomerEntity(
                "Silvi", "087238475673"));

        user = accRepo.get(userId);

        customer = custRepo.get(customerId);
        System.out.println(customer.getid());
    }

    @AfterAll
    public static void tearDown() {
        accRepo.delete(userId);
        custRepo.delete(customerId);
    }

    @Test
    @Order(1)
    public void testAdd() {
        TransactionEntity transaksi = new TransactionEntity(
                new Date(),
                new Date(),
                LaundryStatus.Finish,
                PaymentStatus.Paid,
                23,
                user,
                customer);

        transactionId = repo.add(transaksi);
        assertTrue(transactionId > 0);
    }

    @Test
    @Order(2)
    public void testGet() {
        TransactionEntity trans = repo.get(transactionId);
        TransactionEntity trans2 = repo.get(new HashMap<String, Object>() {
            {
                put("customer_id", customerId);
            }
        }).get(0);

        assertEquals(PaymentStatus.Paid, trans.getPaymentStatus());
        assertEquals(23, trans2.getamount());

    }

    @Test
    @Order(3)
    public void testGetAll() {
        TransactionEntity trans = new TransactionEntity(
                new Date(),
                new Date(),
                LaundryStatus.Process,
                PaymentStatus.Unpaid,
                233,
                user,
                customer);

        assertTrue(repo.add(trans) > 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test
    @Order(4)
    public void testSearch() {
        TransactionEntity trans2 = repo.search(new HashMap<String, Object>() {
            {
                put("customer_id", customerId);
            }
        }).get(0);

        assertEquals(23, trans2.getamount());
    }

    @Test
    @Order(5)
    public void testSearchUser() {
        TransactionEntity trans2 = repo.searchByUser(user, new HashMap<String, Object>() {
            {
                put("user_id", userId);
            }
        }).get(0);

        assertEquals(23, trans2.getamount());
    }

    @Test
    @Order(6)
    public void testUpdate() {
        TransactionEntity trans = repo.get(transactionId);
        trans.setamount(1234);

        assertTrue(repo.Update(trans));
        trans = repo.get(transactionId);

        assertNotEquals(23, trans.getamount());
        assertEquals(1234, trans.getamount());
    }

    @Test
    @Order(7)
    public void testDelete() {
        assertTrue(repo.delete(transactionId));
    }
}
