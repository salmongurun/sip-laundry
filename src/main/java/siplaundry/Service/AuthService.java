package siplaundry.Service;

import java.util.HashMap;
import java.util.List;

import siplaundry.data.SessionData;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;

public class AuthService {

    private static UsersRepo srv = new UsersRepo();

    public boolean login(String name, String password){
        List<UserEntity> acc =  srv.get(new HashMap<String, Object>(){
            {
            put("fullname", name);
            put("password", password);
            }
        });

        if(acc.size() > 0){
            return true;
        } 
        SessionData.user = acc.get(0);
        return false;
    }
}
