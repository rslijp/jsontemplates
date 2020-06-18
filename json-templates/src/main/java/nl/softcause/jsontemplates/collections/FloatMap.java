package nl.softcause.jsontemplates.collections;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class FloatMap implements Map<String, Float> {

    @Delegate
    private Map<String, Float> base = new HashMap<>();

}
