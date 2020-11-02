package nl.softcause.workbench;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class HashMapWorkBenchStore implements IWorkBenchStore {
    private Map<String, DatabaseEntry> DATABASE = new ConcurrentHashMap<>();

    @Override
    public void put(String token, DatabaseEntry databaseEntry) {
        DATABASE.put(token, databaseEntry);
    }

    @Override
    public boolean containsKey(String token) {
        return DATABASE.containsKey(token);
    }

    @Override
    public DatabaseEntry get(String token) {
        return DATABASE.get(token).clone();
    }

    @Override
    public void remove(String token) {
        DATABASE.remove(token);
    }
}