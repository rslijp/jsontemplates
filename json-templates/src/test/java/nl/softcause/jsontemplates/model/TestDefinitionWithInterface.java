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

public class TestDefinitionWithInterface implements IHaveName{

    @Setter
    @Getter
    String name;

    @Setter
    @Getter
    int age;

}
