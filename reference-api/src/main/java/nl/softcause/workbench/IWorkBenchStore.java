package nl.softcause.workbench;

import nl.softcause.workbench.DatabaseEntry;

public interface IWorkBenchStore {
    void put(String token, DatabaseEntry databaseEntry);

    boolean containsKey(String token);

    DatabaseEntry get(String token);

    void remove(String token);
}
