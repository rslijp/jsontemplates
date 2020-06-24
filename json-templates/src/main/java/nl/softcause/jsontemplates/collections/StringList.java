package nl.softcause.jsontemplates.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class StringList implements List<String> {

    @Delegate
    private List<String> base = new ArrayList<>();

    public StringList(String...values) {
        base.addAll(Arrays.asList(values));
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof StringList) {
            return base.equals(((StringList) o).base);
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
