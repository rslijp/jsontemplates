package nl.softcause.jsontemplates.nodes.types;

import java.text.MessageFormat;

import lombok.NonNull;
import lombok.Value;

@Value
public class SinglePositionSlot implements ISlotPattern {

    @NonNull
    private final ISlotPattern basePattern;

    @Override
    public String match(String slotName, Class clazz, int nodesInSlot) {
        if (clazz == null) {
            return null;
        }
        if (nodesInSlot > 1) {
            return error(slotName);
        }
        return basePattern.match(slotName, clazz, nodesInSlot);
    }

    public String error(String slotName) {
        return MessageFormat.format("Only one entry is allowed for slot {0}", slotName);
    }

    @Override
    public String getDescription() {
        return basePattern.getDescription() + "[1]";
    }

    @Override
    public Class[] getLimit() {
        return basePattern.getLimit();
    }


}
