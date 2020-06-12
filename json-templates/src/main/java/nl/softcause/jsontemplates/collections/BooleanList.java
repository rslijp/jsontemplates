package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class BooleanList implements List<Boolean> {

    @Delegate
    private List<Boolean> base = new ArrayList<>();

    public BooleanList(Boolean...values) {
        base.addAll(Arrays.asList(values));
    }
}
