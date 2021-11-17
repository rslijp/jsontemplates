package nl.softcause.jsontemplates.model;

import lombok.Getter;
import lombok.Setter;

public class TestDefinitionWithExtendingInterfaceContainer {

    @Setter
    @Getter
    IHaveAgeAndName nested;

}
