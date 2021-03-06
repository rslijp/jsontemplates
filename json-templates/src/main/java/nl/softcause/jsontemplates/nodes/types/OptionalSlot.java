package nl.softcause.jsontemplates.nodes.types;

import lombok.NonNull;
import lombok.Value;

@Value
public class OptionalSlot implements ISlotPattern {

    @NonNull
    private final ISlotPattern basePattern;

    @Override
    public String match(String slotName, Class clazz, int nodesInSlot) {
        if (clazz == null) {
            return null;
        }
        return basePattern.match(slotName, clazz, nodesInSlot);
    }

    @Override
    public String getDescription() {
        return basePattern.getDescription() + "?";
    }

    @Override
    public Class[] getLimit() {
        return basePattern.getLimit();
    }
}
