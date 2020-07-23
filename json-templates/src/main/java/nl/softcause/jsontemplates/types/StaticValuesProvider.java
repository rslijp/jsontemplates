package nl.softcause.jsontemplates.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StaticValuesProvider<T> implements IAllowedValuesProvider {

    private final List<Object> values;

    @SuppressWarnings("WeakerAccess")
    public StaticValuesProvider(List<T> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public StaticValuesProvider(T... values) {
        this(Arrays.asList(values));
    }


    @Override
    public List<Object> valuesFor(String context, String discriminator) {
        return values;
    }

    @Override
    public List<AllowedValueSets> allValues() {
        return Collections.singletonList(new AllowedValueSets(null, null, values));
    }

}
