package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongList implements List<Long> {

    @Delegate
    private List<Long> base = new ArrayList<>();

    public LongList(Long...values){
        base.addAll(Arrays.asList(values));
    }
}
