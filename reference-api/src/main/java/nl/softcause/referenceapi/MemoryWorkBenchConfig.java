package nl.softcause.referenceapi;

import nl.softcause.workbench.HashMapWorkBenchStore;
import nl.softcause.workbench.IWorkBenchStore;
import nl.softcause.workbench.SerializingHashMapWorkBenchStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MemoryWorkBenchConfig {

    @Bean
    public IWorkBenchStore hashMapWorkBenchStore() {
        return new SerializingHashMapWorkBenchStore();
    }

}
