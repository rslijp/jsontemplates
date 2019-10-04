package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class IntegerMap implements Map<String, Integer> {

    @Delegate
    private Map<String,Integer> base = new HashMap<>();

}
