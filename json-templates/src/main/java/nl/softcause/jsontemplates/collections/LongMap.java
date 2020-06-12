package nl.softcause.jsontemplates.collections;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class LongMap implements Map<String, Long> {

    @Delegate
    private Map<String, Long> base = new HashMap<>();

}
