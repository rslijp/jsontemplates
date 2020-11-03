package nl.softcause.workbench;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class SerializingHashMapWorkBenchStore implements IWorkBenchStore {
    private Map<String, String> DATABASE = new ConcurrentHashMap<>();

    @SneakyThrows
    @Override
    public void put(String token, DatabaseEntry databaseEntry) {
        DATABASE.put(token, new ObjectMapper().writeValueAsString(databaseEntry));
    }

    @Override
    public boolean containsKey(String token) {
        return DATABASE.containsKey(token);
    }

    @SneakyThrows
    @Override
    public DatabaseEntry get(String token) {
        return new ObjectMapper().readValue(DATABASE.get(token),DatabaseEntry.class);
    }

    @Override
    public void remove(String token) {
        DATABASE.remove(token);
    }
}