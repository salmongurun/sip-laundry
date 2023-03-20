package siplaundry.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import siplaundry.data.AccountRole;
import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.UserEntity;
import siplaundry.entity.TransactionEntity;

public class TransDetailRepoTest {
    private static CustomerEntity customer;
    private static UserEntity user;
    private static LaundryEntity laundry;
    private static TransactionEntity transaction;

    private static LaundryRepo laundryRepo = new LaundryRepo();
    private static UsersRepo userRepo = new UsersRepo();
    private static CustomerRepo custRepo = new CustomerRepo();
    private static TransactionRepo transRepo = new TransactionRepo();
    private static TransactionDetailRepo repo = new TransactionDetailRepo();
    private static Integer userId, customerId, laundryId, transactionId;

    @BeforeAll
    public static void init() {
        userId = userRepo.add(new UserEntity(
                "David", "324567", "david", "jember", AccountRole.cashier));

        customerId = custRepo.add(new CustomerEntity(
                "Angel", "098765897652"));

        laundryId = laundryRepo.add(new LaundryEntity(
                "biji", 50000, "Batik"));

        user = userRepo.get(userId);
        customer = custRepo.get(customerId);
        laundry = laundryRepo.get(laundryId);

        transactionId = transRepo.add(new TransactionEntity(
                new Date(),
                new Date(),
                LaundryStatus.Finish,
                PaymentStatus.Paid,
                23,
                user,
                customer));

        transaction = transRepo.get(transactionId);
    }

    @AfterAll
    public static void tearDown() {
        transRepo.delete(transactionId);
        custRepo.delete(customerId);
        userRepo.delete(userId);
        laundryRepo.delete(laundryId);
    }

    @Test
    @Order(1)
    public void testAdd() {
        TransactionDetailEntity detail = new TransactionDetailEntity(
                transaction,
                laundry,
                3);

        assertTrue(repo.add(detail) != 0);
    }

    @Test
    @Order(2)
    public void testGetAll() {
        TransactionDetailEntity detail = new TransactionDetailEntity(
                transaction,
                laundry,
                5);
        TransactionDetailEntity detail2 = repo.get(new HashMap<String, Object>() {
            {
                put("transaction_id", transactionId);
            }
        }).get(0);

        assertTrue(repo.add(detail) > 0);
        assertTrue(repo.get().size() > 1);
        assertEquals(3, detail2.getQty());

    }

    @Test
    @Order(3)
    public void testSearch() {
        TransactionDetailEntity detail2 = repo.search(new HashMap<String, Object>() {
            {
                put("transaction_id", transactionId);
            }
        }).get(0);

        assertEquals(3, detail2.getQty());
    }
}
