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

public class TestDefinition {

    @Setter
    @Getter
    String name;
    @Getter
    String nameGet;
    @Setter
    String nameSet;
    @Setter
    @Getter
    int age;
    @Setter
    @Getter
    Integer mentalAge;
    @Setter
    @Getter
    IntegerList magicNumbers;
    @Setter
    @Getter
    StringList titles;
    @Setter
    @Getter
    String[] certificates;
    @Setter
    @Getter
    TestNestedDefinition nested;
    @Setter
    @Getter
    TestNestedDefinition[] children;
    @Setter
    @Getter
    TestNestedDefinitionList other;
    @Setter
    @Getter
    TestNestedDefinitionMap map;
    @Setter
    @Getter
    IntegerMap moreMagic;

    public static class TestNestedDefinitionList implements List<TestNestedDefinition> {
        @Delegate
        private List<TestNestedDefinition> base = new ArrayList<>();
    }

    static class TestNestedDefinitionMap implements Map<String, TestNestedDefinition> {
        @Delegate
        private Map<String, TestNestedDefinition> base = new HashMap<>();
    }
}
