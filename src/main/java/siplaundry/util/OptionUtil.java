package siplaundry.util;

import siplaundry.entity.OptionEntity;
import siplaundry.repository.OptionRepo;

public class OptionUtil {
    OptionRepo optRepo = new OptionRepo();
    OptionEntity opt;

    public String getName(){
        opt =  optRepo.get("name");
        return opt.getValue();
    }

    public String getAdress(){
        opt = optRepo.get("address");
        return opt.getValue();
    }

    
}
