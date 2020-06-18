package nl.softcause.jsontemplates.collections;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.Delegate;

public class InstantList implements List<Instant> {

    @Delegate
    private List<Instant> base = new ArrayList<>();

    public InstantList(Instant...values) {
        base.addAll(Arrays.asList(values));
    }
}
