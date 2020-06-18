package nl.softcause.jsontemplates.nodes.types;

import java.util.Arrays;

import lombok.Value;

@Value
public class LimitedSlot implements ISlotPattern {

    private Class[] limit;

    @Override
    public boolean match(Object object) {
        if (object == null) {
            return false;
        }
        return Arrays.asList(limit).contains(object.getClass());
    }

    @Override
    public String getDescription() {
        return String.join("|", Arrays.stream(limit).map(Class::getName).toArray(String[]::new));
    }

    @Override
    public ISlotPattern getBasePattern() {
        return this;
    }
}
