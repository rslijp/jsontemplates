package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class ObjectList implements List<Object> {

    @Delegate
    private List<Object> base = new ArrayList<>();

    public ObjectList(Object...values) {
        base.addAll(Arrays.asList(values));
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ObjectList) {
            return base.equals(((ObjectList) o).base);
        }
        return false;
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
