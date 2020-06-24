package nl.softcause.jsontemplates.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import nl.softcause.jsontemplates.collections.StringList;

public class TestDefinitionWithIgnore {

    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    @IgnoreIntrospection
    TestNestedDefinition nested;

}
