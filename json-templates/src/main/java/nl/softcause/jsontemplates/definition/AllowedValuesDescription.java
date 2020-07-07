package nl.softcause.jsontemplates.definition;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.types.AllowedValueSets;

@Getter
@EqualsAndHashCode
class AllowedValuesDescription {


    AllowedValuesDescription(String name, String discriminatorField, List<AllowedValueSets> valueSet) {
        this.name = name;
        this.discriminatorField = discriminatorField;
        this.valueSet = valueSet;
    }

    private final String name;
    private final String discriminatorField;
    private final List<AllowedValueSets> valueSet;

    @Override
    public String toString() {
        return String.format("%s %s? -> %s", name, discriminatorField, valueSet.size());
    }

}
