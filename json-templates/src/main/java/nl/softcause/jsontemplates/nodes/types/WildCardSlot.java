package nl.softcause.jsontemplates.nodes.types;

import lombok.Value;

@Value
public class WildCardSlot implements ISlotPattern {

    @Override
    public boolean match(Object object) {
        return object!=null;
    }

    @Override
    public String getDescription() {
        return "*";
    }

    @Override
    public ISlotPattern getBasePattern() {
        return this;
    }
}
