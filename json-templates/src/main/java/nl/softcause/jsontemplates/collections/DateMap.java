package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.*;

public class DateMap implements Map<String,Date> {

    @Delegate
    private Map<String,Date> base = new HashMap<>();

}
