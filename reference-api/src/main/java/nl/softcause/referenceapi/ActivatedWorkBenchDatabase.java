package nl.softcause.referenceapi;

import lombok.NonNull;
import nl.softcause.workbench.IWorkBenchStore;
import nl.softcause.workbench.WorkBenchDatabase;
import org.springframework.stereotype.Component;

@Component
public class ActivatedWorkBenchDatabase extends WorkBenchDatabase {

    public ActivatedWorkBenchDatabase(@NonNull IWorkBenchStore database) {
        super(database);
    }
}