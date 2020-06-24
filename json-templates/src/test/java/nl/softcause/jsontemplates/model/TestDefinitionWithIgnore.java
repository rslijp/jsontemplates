package nl.softcause.jsontemplates.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class TestDefinitionWithIgnore {

    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    @IgnoreIntrospection
    TestNestedDefinition nested;

    @Getter
    @Setter
    @IgnoreIntrospection
    TestNestedDefinition[] nestedArray;

    @Getter
    @Setter
    @IgnoreIntrospection
    List<TestNestedDefinition> nestedList;

    @Getter
    @Setter
    TestDefinitionWithIgnore recursion;

}
