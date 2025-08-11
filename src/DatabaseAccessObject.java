import java.util.ArrayList;

public interface DatabaseAccessObject<T> {

	T get(int id);
    
    ArrayList<T> getAll();
    
    void create(T t);
    
    void update(T t, String params);
    
    void delete(T t);
}
