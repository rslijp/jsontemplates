package nl.softcause.jsontemplates.collections;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class CalenderMap implements Map<String, Calendar> {

    @Delegate
    private Map<String, Calendar> base = new HashMap<>();

}
