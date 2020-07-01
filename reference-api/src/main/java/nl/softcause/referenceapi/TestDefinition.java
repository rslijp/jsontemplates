package nl.softcause.referenceapi;

import lombok.Getter;
import lombok.Setter;
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

//        @Setter
//        @Getter
//        TestNestedDefinitionList other;
//
//        @Setter
//        @Getter
//        TestNestedDefinitionMap map;

    @Setter
    @Getter
    IntegerMap moreMagic;
}
