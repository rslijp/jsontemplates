package nl.softcause.jsontemplates.model;

import lombok.Getter;
import lombok.Setter;

public class TestDefinitionWithExtendingInterface implements IHaveAgeAndName{

    @Setter
    @Getter
    String name;

    @Setter
    @Getter
    int age;

}
