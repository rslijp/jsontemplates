package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;

public class StringMap implements Map<String, String> {

    @Delegate
    private Map<String,String> base = new HashMap<>();

}
