package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class IntegerList implements List<Integer> {

    @Delegate
    private List<Integer> base = new ArrayList<>();

    public IntegerList(Integer...values) {
        base.addAll(Arrays.asList(values));
    }

    @Override
    public boolean equals(Object rhs) {
        if(rhs instanceof IntegerList) {
            return base.equals(rhs);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return base.hashCode();
    }

    @Override
    public String toString() {
        return base.toString();
    }
}
