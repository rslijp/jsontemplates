package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class FloatList implements List<Float> {

    @Delegate
    private List<Float> base = new ArrayList<>();

    public FloatList(Float...values) {
        base.addAll(Arrays.asList(values));
    }
}
