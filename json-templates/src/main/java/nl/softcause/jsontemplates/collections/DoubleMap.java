package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoubleMap implements Map<String,Double> {

    @Delegate
    private Map<String,Double> base = new HashMap<>();

}
