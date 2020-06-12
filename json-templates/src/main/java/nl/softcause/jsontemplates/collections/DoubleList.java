package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class DoubleList implements List<Double> {

    @Delegate
    private List<Double> base = new ArrayList<>();

    public DoubleList(Double...values) {
        base.addAll(Arrays.asList(values));
    }
}
