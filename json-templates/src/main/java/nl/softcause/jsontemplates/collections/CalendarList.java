package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lombok.experimental.Delegate;

public class CalendarList implements List<Calendar> {

    @Delegate
    private List<Calendar> base = new ArrayList<>();

    public CalendarList(Calendar...values) {
        base.addAll(Arrays.asList(values));
    }
}
