package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class LongList implements List<Long> {

    @Delegate
    private List<Long> base = new ArrayList<>();

    public LongList(Long...values) {
        base.addAll(Arrays.asList(values));
    }
}
