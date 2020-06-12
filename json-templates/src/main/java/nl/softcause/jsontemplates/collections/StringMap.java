package nl.softcause.jsontemplates.collections;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class StringMap implements Map<String, String> {

    @Delegate
    private Map<String, String> base = new HashMap<>();

}
