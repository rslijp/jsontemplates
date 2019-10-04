package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.*;

public class BooleanMap implements Map<String,Boolean> {

    @Delegate
    private Map<String,Boolean> base = new HashMap<>();

}
