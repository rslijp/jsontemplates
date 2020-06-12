package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.experimental.Delegate;

public class DateList implements List<Date> {

    @Delegate
    private List<Date> base = new ArrayList<>();

    public DateList(Date...values) {
        base.addAll(Arrays.asList(values));
    }
}
