package nl.softcause.jsontemplates.nodes.types;

import java.util.Arrays;

import lombok.Value;

@Value
public class LimitedSlot implements ISlotPattern {

    private Class[] limit;

    @Override
    public String match(String slotName, Class clazz, int nodesInSlot) {
        if (clazz == null) {
            return "Node for slot is empty";
        }
        if (Arrays.asList(limit).contains(clazz)) {
            return null;
        }
        return error(slotName, clazz);
    }

    @Override
    public String getDescription() {
        return String.join("|", Arrays.stream(limit).map(Class::getName).toArray(String[]::new));
    }

    @Override
    public ISlotPattern getBasePattern() {
        return this;
    }

    public String error(String name, Class actual) {
        return String.format("Slot '%s' only accepts '%s' but found '%s'", name, getDescription(),
                actual.getSimpleName());
    }
}
