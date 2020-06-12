package nl.softcause.jsontemplates.collections;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.experimental.Delegate;

public class DateMap implements Map<String, Date> {

    @Delegate
    private Map<String, Date> base = new HashMap<>();

}
