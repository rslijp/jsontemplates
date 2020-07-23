package nl.softcause.jsontemplates.types;

import java.util.List;

public interface IAllowedValuesProvider {

    List<Object> valuesFor(String context, String discriminator);

    List<AllowedValueSets> allValues();
}
