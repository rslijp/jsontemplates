package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateList implements List<Date> {

    @Delegate
    private List<Date> base = new ArrayList<>();

    public DateList(Date...values){
        base.addAll(Arrays.asList(values));
    }
}
