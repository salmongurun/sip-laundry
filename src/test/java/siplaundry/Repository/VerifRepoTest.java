package siplaundry.Repository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import siplaundry.Entity.UserEntity;
import siplaundry.Entity.VerivicationEntity;
import siplaundry.data.AccountRole;
import siplaundry.repository.UsersRepo;
import siplaundry.repository.VerivicationRepo;

public class VerifRepoTest {
    private static VerivicationRepo repo = new VerivicationRepo();
    private static UsersRepo userRepo = new UsersRepo();
    private static Map<String, Object> keywords;

    private static UserEntity user;
    private static Integer userId;
    private static String code = "087653";

    @BeforeAll
    public static void init() {
        userId = userRepo.add(new UserEntity(
            "Ahkam", "54545343", "ahkam", "jember", AccountRole.admin
        ));

        user = userRepo.get(userId);
        keywords = new HashMap<String, Object>() {{
            put("user_id", userId);
            put("code", code);
        }};
    }

    @AfterAll
    public static void tearDown() {
        userRepo.delete(userId);
    }

    @Test @Order(1)
    public void testAdd() {
        VerivicationEntity verify = new VerivicationEntity(
            user,
            "087653"
        );

        assertTrue(repo.add(verify) != 0);
    }

    @Test @Order(2)
    public void testGet() {
        VerivicationEntity verify = repo.get(keywords).get(0);

        assertEquals(code, verify.getCode());
    }

    @Test @Order(3)
    public void testGetAll() {
        String code2 = "087653";
        VerivicationEntity verify = new VerivicationEntity(user, code2);

        repo.add(verify);
        List<VerivicationEntity> verifies = repo.get();

        assertTrue(verifies.size() > 1);
        assertEquals(code, verifies.get(1).getCode());  //tpi nilainya ngga berubah
    }

    @Test @Order(4)
    public void testUpdate() {
        String codeNew = "087656";
        VerivicationEntity verify = repo.get(keywords).get(0);

        verify.setCode(codeNew);
        keywords.put("code", codeNew);
        repo.Update(verify);
        verify = repo.get(keywords).get(0);

        assertEquals(codeNew, verify.getCode());
        code = codeNew;
    }

    @Test @Order(5)
    public void testDeleteverify() {
        VerivicationEntity verify = repo.get(keywords).get(0);
        assertTrue(repo.delete(verify));
    }

    @Test @Order(6)
    public void testDeleteId(){
        assertTrue(repo.delete(userId));

    }
}
