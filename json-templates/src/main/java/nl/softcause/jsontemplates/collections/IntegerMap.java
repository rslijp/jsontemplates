package nl.softcause.jsontemplates.collections;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class IntegerMap implements Map<String, Integer> {

    @Delegate
    private Map<String, Integer> base = new HashMap<>();

}
