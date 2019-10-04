package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;

public class FloatMap implements Map<String,Float> {

    @Delegate
    private Map<String,Float> base = new HashMap<>();

}
