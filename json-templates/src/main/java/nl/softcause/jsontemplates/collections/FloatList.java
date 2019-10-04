package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloatList implements List<Float> {

    @Delegate
    private List<Float> base = new ArrayList<>();

    public FloatList(Float...values){
        base.addAll(Arrays.asList(values));
    }
}
