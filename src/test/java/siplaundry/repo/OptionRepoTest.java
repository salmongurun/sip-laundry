package siplaundry.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import siplaundry.entity.OptionEntity;
import siplaundry.repository.OptionRepo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OptionRepoTest {
    private static OptionRepo repo = new OptionRepo();
    private static String key2;

    @Test
    @Order(1)
    public void testAdd() {
        OptionEntity option = new OptionEntity(
                "alamat20",
                "sumbersari");
        assertTrue(repo.add(option) != 0);
    }

    @Test
    @Order(2)
    public void testGetAll() {
        OptionEntity option = new OptionEntity(
                "alamat21",
                "jember");
        
        key2 = option.getKey();

        assertTrue(repo.add(option) != 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test
    @Order(3)
    public void testGet() {
        OptionEntity option = repo.get(key2);

        assertEquals("jember", option.getValue());
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertTrue(repo.delete(key2));
    }
}
