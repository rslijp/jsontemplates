package nl.softcause.jsontemplates.nodes.types;

import lombok.Value;

@Value
public class WildCardSlot implements ISlotPattern {

    @Override
    public String match(String slotName, Class clazz, @SuppressWarnings("unused") int nodesInSlot) {
        return clazz == null ? "Node for slot is empty" : null;
    }

    @Override
    public String getDescription() {
        return "*";
    }

    @Override
    public ISlotPattern getBasePattern() {
        return this;
    }

    @Override
    public Class[] getLimit() {
        return null;
    }
}
