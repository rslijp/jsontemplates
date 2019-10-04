package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;

public class LongMap implements Map<String, Long> {

    @Delegate
    private Map<String,Long> base = new HashMap<>();

}
