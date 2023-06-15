package siplaundry.repo;

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
import siplaundry.data.Laundryunit;
import siplaundry.data.PaymentStatus;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.LaundryEntity;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.UserEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.repository.LaundryRepo;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.repository.TransactionRepo;
import siplaundry.repository.UsersRepo;
import siplaundry.entity.TransactionEntity;

public class TransDetailRepoTest {
    private static CustomerEntity customer;
    private static UserEntity user;
    private static LaundryEntity laundry1;
    private static LaundryEntity laundry2;
    private static TransactionEntity transaction;

    private static LaundryRepo laundryRepo = new LaundryRepo();
    private static UsersRepo userRepo = new UsersRepo();
    private static CustomerRepo custRepo = new CustomerRepo();
    private static TransactionRepo transRepo = new TransactionRepo();
    private static TransactionDetailRepo repo = new TransactionDetailRepo();
    private static Integer userId, customerId, laundryId1, laundryId2, transactionId;

    @BeforeAll
    public static void init() {
        userId = userRepo.add(new UserEntity(
            "David", "David Ahkam", "324567", "david", "jember", AccountRole.cashier));

        customerId = custRepo.add(new CustomerEntity(
                "Angel1", "098765897652", "jember"));

        laundryId1 = laundryRepo.add(new LaundryEntity(
                Laundryunit.pcs, 50000, "Batik", true));

        laundryId2 = laundryRepo.add(new LaundryEntity(
                Laundryunit.meter, 5000, "korden", false));

        user = userRepo.get(userId);
        customer = custRepo.get(customerId);
        laundry1 = laundryRepo.get(laundryId1);
        laundry2 = laundryRepo.get(laundryId2);

        transactionId = transRepo.add(new TransactionEntity(
                new Date(),
                1,
                new Date(),
                LaundryStatus.finish,
                PaymentStatus.paid,
                23,
                7000,
                user,
                customer));

        transaction = transRepo.get(transactionId);
    }

    @AfterAll
    public static void tearDown() {
        transRepo.delete(transactionId);
        custRepo.delete(customerId);
        userRepo.delete(userId);
        laundryRepo.delete(laundryId1);
        laundryRepo.delete(laundryId2);
    }

    @Test
    @Order(1)
    public void testAdd() {
        TransactionDetailEntity detail = new TransactionDetailEntity(
                transaction,
                laundry1,
                3);

        int coba = repo.add(detail);
        assertTrue(coba != 0);
    }

    @Test
    @Order(2)
    public void testGetAll() {
        TransactionDetailEntity detail = new TransactionDetailEntity(
                transaction,
                laundry2,
                5);

        assertEquals(5, detail.getQty());
        assertTrue(repo.add(detail) != 0);
        assertTrue(repo.get().size() > 1);

    }

    @Test
    @Order(3)
    public void testGet(){
    TransactionDetailEntity detail2 = repo.get(new HashMap<String, Object>() {
        {
            put("transaction_id", transactionId);
        }
    }).get(0);

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
