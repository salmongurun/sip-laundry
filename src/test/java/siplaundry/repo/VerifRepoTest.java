package siplaundry.repo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.entity.VerificationEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.repository.VerificationRepo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VerifRepoTest {
    private static VerificationRepo repo = new VerificationRepo();
    private static UsersRepo userRepo = new UsersRepo();
    private static Map<String, Object> keywords;

    private static UserEntity user1;
    private static UserEntity user2;
    private static Integer userId1;
    private static Integer userId2;
    private static String code = "087653";

    @BeforeAll
    public static void init() {
        userId1 = userRepo.add(new UserEntity(
            "Ahkam1", "Ahkam Hafidz", "54545343", "ahkam", "jember", AccountRole.admin));
        userId2 = userRepo.add(new UserEntity(
            "Ahkam2", "Ahkam Hafidz", "54545343", "ahkam", "jember", AccountRole.admin));

        user1 = userRepo.get(userId1);
        user2 = userRepo.get(userId2);
        keywords = new HashMap<String, Object>() {
            {
                put("user_id", userId1);
                put("code", code);
            }
        };
    }

    @AfterAll
    public static void tearDown() {
        userRepo.delete(userId1);
        userRepo.delete(userId2);
    }

    @Test
    @Order(1)
    public void testAdd() {
        VerificationEntity verify = new VerificationEntity(
                user1,
                "087653");

        assertTrue(repo.add(verify) != 0);
    }

    @Test
    @Order(2)
    public void testGet() {
        VerificationEntity verify = repo.get(keywords).get(0);

        assertEquals(code, verify.getCode());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        String code2 = "087653";
        VerificationEntity verify = new VerificationEntity(user2, code2);

        repo.add(verify);
        List<VerificationEntity> verifies = repo.get();

        assertTrue(verifies.size() > 1);
//        assertEquals(code, verifies.get(1).getCode()); // tpi nilainya ngga berubah
    }

    @Test
    @Order(4)
    public void testUpdate() {
        String codeNew = "087656";
        VerificationEntity verify = repo.get(keywords).get(0);

        verify.setCode(codeNew);
        keywords.put("code", codeNew);
        repo.Update(verify);
        verify = repo.get(keywords).get(0);

        assertEquals(codeNew, verify.getCode());
        code = codeNew;
    }

    @Test
    @Order(5)
    public void testDeleteverify() {
        VerificationEntity verify = repo.get(keywords).get(0);
        assertTrue(repo.delete(verify));
    }

    @Test
    @Order(6)
    public void testDeleteId() {
        assertTrue(repo.delete(userId2));
        
    }
}
