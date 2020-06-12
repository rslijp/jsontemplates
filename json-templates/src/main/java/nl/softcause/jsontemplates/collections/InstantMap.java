package nl.softcause.jsontemplates.collections;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class InstantMap implements Map<String, Instant> {

    @Delegate
    private Map<String, Instant> base = new HashMap<>();

}
