package nl.softcause.jsontemplates.collections;


import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class BooleanMap implements Map<String, Boolean> {

    @Delegate
    private Map<String, Boolean> base = new HashMap<>();

}
