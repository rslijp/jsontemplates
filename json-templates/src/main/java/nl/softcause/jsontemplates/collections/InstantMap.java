package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class InstantMap implements Map<String, Instant> {

    @Delegate
    private Map<String,Instant> base = new HashMap<>();

}
