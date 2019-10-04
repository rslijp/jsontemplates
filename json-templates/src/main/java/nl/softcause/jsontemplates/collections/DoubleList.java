package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleList implements List<Double> {

    @Delegate
    private List<Double> base = new ArrayList<>();

    public DoubleList(Double...values){
        base.addAll(Arrays.asList(values));
    }
}
