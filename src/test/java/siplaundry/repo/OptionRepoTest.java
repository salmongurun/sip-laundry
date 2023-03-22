package siplaundry.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import siplaundry.entity.OptionEntity;
import siplaundry.repository.OptionRepo;

public class OptionRepoTest {
    private static OptionRepo repo = new OptionRepo();
    private static String key;

    @Test
    @Order(1)
    public void testAdd() {
        OptionEntity option = new OptionEntity(
                "alamat15",
                "sumbersari");
        key = option.getKey();
        assertTrue(repo.add(option) != 0);
    }

    @Test
    @Order(2)
    public void testGetAll() {
        OptionEntity option = new OptionEntity(
                "alamat19",
                "jember");

        assertTrue(repo.add(option) != 0);
        assertTrue(repo.get().size() > 1);
    }

    @Test
    @Order(3)
    public void testGet() {
        OptionEntity option = repo.get(key);

        assertEquals("sumbersari", option.getValue());
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertTrue(repo.delete(key));
    }
}
