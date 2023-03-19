package siplaundry.repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import siplaundry.Entity.Entity;
import siplaundry.Util.DatabaseUtil;

public interface Repository<E extends Entity> {
    final Connection conn = DatabaseUtil.getConnection();

    public List<E> getAll();

    public E get(Integer id);

    public List<E> get(Map<String, Object> values);

    public List<E> search(Map<String, Object> values);

    public Integer add(E entity);

    public boolean Update(E entity);

    public boolean delete(int id);


}
