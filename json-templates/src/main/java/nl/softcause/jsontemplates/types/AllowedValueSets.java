package nl.softcause.jsontemplates.types;

import java.util.List;

import lombok.Value;

@Value
public class AllowedValueSets {
    private String discriminator;
    private List<Object> values;

    public boolean match(String candidate) {
        if (discriminator == null) {
            return candidate == null;
        }
        return discriminator.equals(candidate);
    }
}
