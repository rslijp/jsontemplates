package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalenderMap implements Map<String, Calendar> {

    @Delegate
    private Map<String,Calendar> base = new HashMap<>();

}
