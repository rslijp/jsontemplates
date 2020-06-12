package nl.softcause.jsontemplates.collections;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class DoubleMap implements Map<String, Double> {

    @Delegate
    private Map<String, Double> base = new HashMap<>();

}
