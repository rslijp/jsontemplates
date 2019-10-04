package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BooleanList implements List<Boolean> {

    @Delegate
    private List<Boolean> base = new ArrayList<>();

    public BooleanList(Boolean...values){
        base.addAll(Arrays.asList(values));
    }
}
