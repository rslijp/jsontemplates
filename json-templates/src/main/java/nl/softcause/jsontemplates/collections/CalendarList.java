package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.*;

public class CalendarList implements List<Calendar> {

    @Delegate
    private List<Calendar> base = new ArrayList<>();

    public CalendarList(Calendar...values){
        base.addAll(Arrays.asList(values));
    }
}
