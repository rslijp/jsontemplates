package nl.softcause.jsontemplates.collections;

import lombok.experimental.Delegate;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstantList implements List<Instant> {

    @Delegate
    private List<Instant> base = new ArrayList<>();

    public InstantList(Instant...values){
        base.addAll(Arrays.asList(values));
    }
}
