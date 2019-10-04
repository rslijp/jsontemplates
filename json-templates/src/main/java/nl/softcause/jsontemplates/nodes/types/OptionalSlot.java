package nl.softcause.jsontemplates.nodes.types;

import lombok.NonNull;
import lombok.Value;

@Value
public class OptionalSlot implements ISlotPattern {

    @NonNull
    private final ISlotPattern basePattern;

    @Override
    public boolean match(Object object) {
        if(object==null) return true;
        return basePattern.match(object);
    }

    @Override
    public String getDescription() {
        return basePattern.getDescription()+"?";
    }
}
