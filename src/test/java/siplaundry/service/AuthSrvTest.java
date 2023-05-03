package siplaundry.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.service.AuthService;

public class AuthSrvTest {
    private static UsersRepo user = new UsersRepo();
    private static boolean login;
    private static AuthService Auth = new AuthService();
    private static UserEntity userEn;

    /**
     *
     */
    @BeforeAll
    public static void init(){
         userEn = new UserEntity(
            "Sinta",
            "sinta manila",
            "089787675654",
            "kamu",
            "jember",
            AccountRole.admin
        );
        user.add(userEn);
    }

    @Test
    public void LoginTest(){
        login = Auth.login(userEn.getUsername(), userEn.getPassword());
        assertTrue(login);
    }


}
