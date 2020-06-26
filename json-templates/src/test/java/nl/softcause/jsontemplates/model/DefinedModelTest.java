package nl.softcause.jsontemplates.model;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.softcause.jsontemplates.collections.BeanConverters;
import nl.softcause.jsontemplates.collections.IntegerList;
import nl.softcause.jsontemplates.collections.IntegerMap;
import nl.softcause.jsontemplates.collections.StringList;
import nl.softcause.jsontemplates.types.TextEnumType;
import nl.softcause.jsontemplates.types.Types;
import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

public class DefinedModelTest {

    static {
        BeanConverters.buildAndRegister(TestDefintionWithEnum.ListTestNum::new, TestEnum.class, TestDefintionWithEnum.ListTestNum.class);
    }

    @Test
    public void Should_retrieve_generic_type() {
        var model = new DefinedModel<>(TestDefinition.class);

        var c = model.getModelDefinition().getModelType();

        assertThat(c.equals(TestDefinition.class), is(true));
    }

    @Test
    public void Should_retrieve_text_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("name");

        assertThat(d, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true, null)));
    }

    @Test
    public void Should_retrieve_getter_only_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("nameGet");

        assertThat(d, is(new DefinitionRegistryEntry("nameGet", Types.OPTIONAL_TEXT, null, true, false, null)));
    }


    @Test
    public void Should_retrieve_setter_only_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("nameSet");

        assertThat(d, is(new DefinitionRegistryEntry("nameSet", Types.OPTIONAL_TEXT, null, false, true, null)));
    }


    @Test
    public void Should_retrieve_int_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("age");

        assertThat(d, is(new DefinitionRegistryEntry("age", Types.INTEGER, null, true, true, null)));
    }


    @Test
    public void Should_retrieve_Integer_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("mentalAge");

        assertThat(d, is(new DefinitionRegistryEntry("mentalAge", Types.OPTIONAL_INTEGER, null, true, true, null)));
    }


    @Test
    public void Should_retrieve_Integer_list_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("magicNumbers");

        assertThat(d, is(new DefinitionRegistryEntry("magicNumbers", Types.LIST_INTEGER, null, true, true, null)));
    }


    @Test
    public void Should_retrieve_StringList_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("titles");

        assertThat(d, is(new DefinitionRegistryEntry("titles", Types.LIST_TEXT, null, true, true, null)));
    }

    @Test
    public void Should_retrieve_StringArray_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("certificates");

        assertThat(d, is(new DefinitionRegistryEntry("certificates", Types.LIST_TEXT, null, true, true, null)));
    }

    @Test
    public void Should_retrieve_NestedModel_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("nested");

        assertThat(d, is(new DefinitionRegistryEntry("nested", Types.OBJECT,
                RegistryFactory.register(TestNestedDefinition.class), true, true, null)));
    }


    @Test
    public void Should_retrieve_ModelList_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("other");

        assertThat(d, is(new DefinitionRegistryEntry("other", Types.LIST_OBJECT,
                RegistryFactory.register(TestNestedDefinition.class), true, true, null)));
    }

    @Test
    public void Should_retrieve_TestNestedDefinition_name_property() {
        var model = new DefinedModel<>(TestNestedDefinition.class);

        var d = model.getDefinition("name");

        assertThat(d, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true, null)));
    }

    @Test
    public void Should_retrieve_RecursiveNestedDefinition_name_property() {
        var model = new DefinedModel<>(RecursiveNestedDefinition.class);

        var d = model.getDefinition("name");

        assertThat(d, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true, null)));
    }

    @Test
    public void Should_retrieve_nested_types() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("nested.name");

        assertThat(d, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true, null)));

    }

    @Test
    public void Should_retrieve_RecursiveNestedDefinition_nested_property() {
        var model = new DefinedModel<>(RecursiveNestedDefinition.class);

        var d = model.getDefinition("nested");

        assertThat(d, is(new DefinitionRegistryEntry("nested", Types.OBJECT,
                RegistryFactory.register(RecursiveNestedDefinition.class), true, true, null)));
    }

    @Test
    public void Should_retrieve_ModelMap_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("map");

        assertThat(d, is(new DefinitionRegistryEntry("map", Types.MAP_OBJECT,
                RegistryFactory.register(TestNestedDefinition.class), true, true, null)));
    }

    @Test
    public void Should_retrieve_nested_property_on_ModelMap() {
        var model = new DefinedModel<>(TestDefinition.class);

        var d = model.getDefinition("map.name");

        assertThat(d, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true, null)));

    }


    @Test
    public void Should_report_errors_on_missing_property() {
        var model = new DefinedModel<>(TestDefinition.class);

        try {
            model.getDefinition("wrong");
            fail();
        } catch (ModelException Te) {
            assertThat(Te.getMessage(), is(ModelException.notFound("wrong", TestDefinition.class).getMessage()));
        }

    }

    @Test
    public void Should_report_errors_on_primitive_nesting() {
        var model = new DefinedModel<>(TestDefinition.class);

        try {
            model.getDefinition("age.wrong");
            fail();
        } catch (ModelException Te) {
            assertThat(Te.getMessage(),
                    is(ModelException.primitiveCantHaveProperty(Types.INTEGER, "age", "age.wrong").getMessage()));
        }
    }

    @Test
    public void Should_get_text_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setName("Ludmilla Petrovna");
        model.load(td);

        var name = model.get("name");

        assertThat(name, is("Ludmilla Petrovna"));
    }

    @Test
    public void Should_not_get_setter_only_property() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        try {
            model.get("nameSet");
            fail();
        } catch (ModelException Me) {
            assertThat(Me.getMessage(), is(ModelException.notReadeable("nameSet", TestDefinition.class).getMessage()));
        }
    }


    @Test
    public void Should_get_age_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setAge(42);
        model.load(td);

        var name = model.get("age");

        assertThat(name, is(42));
    }

    @Test
    public void Should_get_Integer_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMentalAge(42);
        model.load(td);

        var name = model.get("mentalAge");

        assertThat(name, is(42));
    }

    @Test
    public void Should_get_Integer_value_as_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMentalAge(null);
        model.load(td);

        var name = model.get("mentalAge");

        assertThat(name, nullValue());
    }

    @Test
    public void Should_get_IntegerList_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMagicNumbers(new IntegerList(42));
        model.load(td);

        var name = model.get("magicNumbers");

        assertThat(name, is(new IntegerList(42)));
    }

    @Test
    public void Should_get_index_valued_of_IntegerList_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMagicNumbers(new IntegerList(42, 43, 44));
        model.load(td);

        var name = model.get("magicNumbers[1]");

        assertThat(name, is(43L));
    }

    @Test
    public void Should_get_IntegerList_value_as_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMagicNumbers(null);
        model.load(td);

        var name = model.get("magicNumbers");

        assertThat(name, nullValue());
    }

    @Test
    public void Should_get_StringList_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setTitles(new StringList("drs."));
        model.load(td);

        var name = model.get("titles");

        assertThat(name, is(new StringList("drs.")));
    }

    @Test
    public void Should_get_index_valued_of_StringList_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setTitles(new StringList("mr.", "drs.", "dr."));
        model.load(td);

        var name = model.get("titles[2]");

        assertThat(name, is("dr."));
    }


    @Test
    public void Should_get_StringList_value_as_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setTitles(null);
        model.load(td);

        var name = model.get("titles");

        assertThat(name, nullValue());
    }

    @Test
    public void Should_get_StringArray_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(new String[] {"scum"});
        model.load(td);

        var name = model.get("certificates");

        assertThat(name, is(new String[] {"scum"}));
    }

    @Test
    public void Should_get_index_valued_of_StringArray_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(new String[] {"mr.", "drs.", "dr."});
        model.load(td);

        var name = model.get("certificates[2]");

        assertThat(name, is("dr."));
    }


    @Test
    public void Should_get_StringArray_value_as_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(null);
        model.load(td);

        var name = model.get("certificates");

        assertThat(name, nullValue());
    }

    @Test
    public void Should_get_Object_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        var n = new TestNestedDefinition();
        td.setNested(n);
        model.load(td);

        var name = model.get("nested");

        assertThat(name, is(n));
    }

    @Test
    public void Should_get_Object_value_as_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setNested(null);
        model.load(td);

        var name = model.get("nested");

        assertThat(name, nullValue());
    }

    @Test
    public void Should_get_NestedValue_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        var n = new TestNestedDefinition();
        n.setName("Nested");
        td.setNested(n);
        model.load(td);

        var name = model.get("nested.name");

        assertThat(name, is("Nested"));
    }

    @Test
    public void Should_get_NestedValue_of_value_as_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setNested(null);
        model.load(td);

        try {
            model.get("nested.name");
            fail();
        } catch (ModelException Te) {
            assertThat(Te.getMessage(), is(ModelException.nestedNullModel("nested.name").getMessage()));
        }
    }

    @Test
    public void Should_set_text_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("name", "Ludmilla Petrovna");

        assertThat(td.getName(), is("Ludmilla Petrovna"));
    }

    @Test
    public void Should_set_int_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("age", 42);

        assertThat(td.getAge(), is(42));
    }

    @Test
    public void Should_set_Integer_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("mentalAge", 42);

        assertThat(td.getMentalAge(), is(42));
    }

    @Test
    public void Should_set_null_Integer_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMentalAge(42);
        model.load(td);

        model.set("mentalAge", null);

        assertThat(td.getMentalAge(), nullValue());
    }

    @Test
    public void Should_not_set_getter_only_property() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);
        try {
            model.set("nameGet", "x");
            fail();
        } catch (ModelException Me) {
            assertThat(Me.getMessage(), is(ModelException.notWriteable("nameGet", TestDefinition.class).getMessage()));
        }
    }

    @Test
    public void Should_set_IntegerList_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("magicNumbers", new IntegerList(42));

        assertThat(td.getMagicNumbers().size(), is(1));
        assertThat(td.getMagicNumbers().get(0), is(42));
    }

    @Test
    public void Should_set_IntegerList_value_with_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("magicNumbers", new int[] {42});

        assertThat(td.getMagicNumbers().size(), is(1));
        assertThat(td.getMagicNumbers().get(0), is(42));
    }

    @Test
    public void Should_set_IntegerList_value_on_index() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMagicNumbers(new IntegerList(42));
        model.load(td);

        model.set("magicNumbers[0]", 37);

        assertThat(td.getMagicNumbers().size(), is(1));
        assertThat(td.getMagicNumbers().get(0), is(37));
    }

    @Test
    public void Should_set_IntegerList_value_on_index_with_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMagicNumbers(new IntegerList(42));
        model.load(td);

        model.set("magicNumbers[0]", 37L);

        assertThat(td.getMagicNumbers().size(), is(1));
        assertThat(td.getMagicNumbers().get(0), is(37));
    }

    @Test
    public void Should_set_StringList_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("titles", new StringList("drs."));

        assertThat(td.getTitles().size(), is(1));
        assertThat(td.getTitles().get(0), is("drs."));
    }

    @Test
    public void Should_set_StringList_value_with_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("titles", new String[] {"drs."});

        assertThat(td.getTitles().size(), is(1));
        assertThat(td.getTitles().get(0), is("drs."));
    }

    @Test
    public void Should_set_StringList_value_on_index() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setTitles(new StringList("drs."));
        model.load(td);

        model.set("titles[0]", "mr.");

        assertThat(td.getTitles().size(), is(1));
        assertThat(td.getTitles().get(0), is("mr."));
    }

    @Test
    public void Should_set_StringList_value_on_index_with_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setTitles(new StringList("drs."));
        model.load(td);

        model.set("titles[0]", new StringBuffer("mr."));

        assertThat(td.getTitles().size(), is(1));
        assertThat(td.getTitles().get(0), is("mr."));
    }

    @Test
    public void Should_set_StringArray_value_with_array_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("certificates", new String[] {"Math"});

        assertThat(td.getCertificates().length, is(1));
        assertThat(td.getCertificates()[0], is("Math"));
    }

    @Test
    public void Should_set_StringArray_value_with_list_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("certificates", Collections.singletonList("Math"));

        assertThat(td.getCertificates().length, is(1));
        assertThat(td.getCertificates()[0], is("Math"));
    }


    @Test
    public void Should_set_StringArray_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        model.set("certificates", new StringList("Math"));

        assertThat(td.getCertificates().length, is(1));
        assertThat(td.getCertificates()[0], is("Math"));
    }


    @Test
    public void Should_set_StringArray_value_on_index() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(new String[] {"Math"});
        model.load(td);

        model.set("certificates[0]", "English");

        assertThat(td.getCertificates().length, is(1));
        assertThat(td.getCertificates()[0], is("English"));
    }

    @Test
    public void Should_set_StringArray_value_on_index_with_cast() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(new String[] {"Math"});
        model.load(td);

        model.set("certificates[0]", new StringBuffer("English"));

        assertThat(td.getCertificates().length, is(1));
        assertThat(td.getCertificates()[0], is("English"));
    }


    @Test
    public void Should_set_StringArray_value_on_index_to_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(new String[] {"Math"});
        model.load(td);

        model.set("certificates[0]", null);

        assertThat(td.getCertificates().length, is(1));
        assertThat(td.getCertificates()[0], nullValue());
    }


    @Test
    public void Should_set_StringArray_value_to_null() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setCertificates(new String[] {"Math"});
        model.load(td);

        model.set("certificates", null);

        assertThat(td.getCertificates(), nullValue());
    }


    @Test
    public void Should_set_nested_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setNested(new TestNestedDefinition());
        model.load(td);

        model.set("nested.name", "Homer");

        assertThat(td.getNested().getName(), is("Homer"));
    }


    @Test
    public void Should_set_nested_value_of_array() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setChildren(new TestNestedDefinition[] {new TestNestedDefinition()});
        model.load(td);

        model.set("children[0].name", "Homer");

        assertThat(td.getChildren()[0].getName(), is("Homer"));
    }

    @Test
    public void Should_set_nested_value_of_list() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setOther(new TestDefinition.TestNestedDefinitionList());
        td.getOther().add(new TestNestedDefinition());
        model.load(td);

        model.set("other[0].name", "Homer");

        assertThat(td.getOther().get(0).getName(), is("Homer"));
    }

    @Test
    public void Should_set_report_null_values_in_list_access() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        model.load(td);

        try {
            model.set("other[0].name", "Homer");
            fail();
        } catch (ModelException Me) {
            assertThat(Me.getMessage(), is(ModelException.nestedNullModel("other[0].name").getMessage()));
        }
    }

    @Test
    public void Should_get_nested_value_of_map() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        var n = new TestNestedDefinition();
        n.setName("Nested");
        td.setMap(new TestDefinition.TestNestedDefinitionMap());
        td.getMap().put("homer", n);
        model.load(td);


        var name = model.get("map(homer).name");
        assertThat(name, is("Nested"));
    }


    @Test
    public void Should_get_map() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        var n = new TestNestedDefinition();
        n.setName("Nested");
        td.setMap(new TestDefinition.TestNestedDefinitionMap());
        td.getMap().put("homer", n);
        model.load(td);


        var map = (TestDefinition.TestNestedDefinitionMap) model.get("map");
        assertThat(map.get("homer").name, is("Nested"));
    }

    @Test
    public void Should_get_map_definition() {
        var model = new DefinedModel<>(TestDefinition.class);

        var definition = model.getDefinition("map");
        assertThat(definition.getType(), is(Types.MAP_OBJECT));
    }


    @Test
    public void Should_get_map_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        var n = new TestNestedDefinition();
        n.setName("Nested");
        td.setMap(new TestDefinition.TestNestedDefinitionMap());
        td.getMap().put("homer", n);
        model.load(td);


        var map = model.get("map(homer)");
        assertThat(map, is(td.getMap().get("homer")));
    }

    @Test
    public void Should_get_null_value_for_unknown_map_entry() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        var n = new TestNestedDefinition();
        n.setName("Nested");
        td.setMap(new TestDefinition.TestNestedDefinitionMap());
        td.getMap().put("homer", n);
        model.load(td);


        var map = model.get("map(wrong)");
        assertThat(map, nullValue());
    }

    @Test
    public void Should_set_nested_value_of_map() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMap(new TestDefinition.TestNestedDefinitionMap());
        TestNestedDefinition n = new TestNestedDefinition();
        td.getMap().put("homer", n);
        model.load(td);

        model.set("map(homer).name", "Simpson");

        assertThat(n.getName(), is("Simpson"));
    }

    @Test
    public void Should_get_integer_map_value() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMoreMagic(new IntegerMap());
        td.getMoreMagic().put("homer", 42);
        model.load(td);


        var map = model.get("moreMagic(homer)");
        assertThat(map, is(42L));
    }

    @Test
    public void Should_get_null_value_for_unknown_integer_map_entry() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMoreMagic(new IntegerMap());
        td.getMoreMagic().put("homer", 42);
        model.load(td);


        var map = model.get("moreMagic(wrong)");
        assertThat(map, nullValue());
    }

    @Test
    public void Should_set_a_value_of_integer_map() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMoreMagic(new IntegerMap());
        td.getMoreMagic().put("homer", 42);
        model.load(td);

        model.set("moreMagic(homer)", 37L);

        assertThat(td.getMoreMagic().get("homer"), is(37));
    }

    @Test
    public void Should_set_null_value_of_integer_map() {
        var model = new DefinedModel<>(TestDefinition.class);
        var td = new TestDefinition();
        td.setMoreMagic(new IntegerMap());
        td.getMoreMagic().put("homer", 42);
        model.load(td);

        model.set("moreMagic(homer)", null);

        assertThat(td.getMoreMagic().get("homer"), nullValue());
    }

    @Test
    public void Should_handle_enum() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);
        var td = new TestDefintionWithEnum();
        td.setName("bla");
        td.setValue(TestEnum.FIRST);
        model.load(td);

        model.set("value",TestEnum.SECOND);
        assertThat(td.getValue(), is(TestEnum.SECOND));

    }

    @Test
    public void Should_handle_enum_convert_from_string() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);
        var td = new TestDefintionWithEnum();
        td.setName("bla");
        td.setValue(TestEnum.FIRST);
        model.load(td);

        model.set("value",TestEnum.SECOND.name());
        assertThat(td.getValue(), is(TestEnum.SECOND));

    }



    @Test
    public void Should_describe_enum() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);

        var definition = model.getDefinition("value");

        assertThat(definition.getType(), is(Types.OPTIONAL_ENUM));
        assertThat(definition.getName(), is("value"));
        assertThat(definition.getAllowedValues(), is(new String[]{TestEnum.FIRST.name(), TestEnum.SECOND.name()}));

    }

    @Test
    public void Should_describe_enum_array() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);

        var definition = model.getDefinition("valueArray");

        assertThat(definition.getType(), is(Types.LIST_ENUM));
        assertThat(definition.getName(), is("valueArray"));
    }


    @Test
    public void Should_describe_enum_list() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);

        var definition = model.getDefinition("values");

        assertThat(definition.getType(), is(Types.LIST_ENUM));
        assertThat(definition.getName(), is("values"));
    }

    @Test
    public void Should_set_EnumList_value_with_cast() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);
        var td = new TestDefintionWithEnum();
        model.load(td);

        model.set("values", new String[] {"FIRST","SECOND"});

        assertThat(td.getValues().size(), is(2));
        assertThat(td.getValues().get(0), is(TestEnum.FIRST));
        assertThat(td.getValues().get(1), is(TestEnum.SECOND));
    }

    @Test
    public void Should_get_EnumList_value_with_cast() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);
        var td = new TestDefintionWithEnum();
        td.setValues(new TestDefintionWithEnum.ListTestNum());
        td.getValues().add(TestEnum.FIRST);
        td.getValues().add(TestEnum.SECOND);
        model.load(td);

        var list = (TestDefintionWithEnum.ListTestNum) model.get("values");

        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(TestEnum.FIRST));
        assertThat(list.get(1), is(TestEnum.SECOND));
    }

    @Test
    public void Should_set_EnumList_value_with_cast_on_index() {
        var model = new DefinedModel<>(TestDefintionWithEnum.class);
        var td = new TestDefintionWithEnum();
        td.setValues(new TestDefintionWithEnum.ListTestNum(TestEnum.SECOND));
        model.load(td);

        model.set("values[0]", TestEnum.FIRST);

        assertThat(td.getValues().size(), is(1));
        assertThat(td.getValues().get(0), is(TestEnum.FIRST));
    }

    @Test
    public void Should_ignore_model_parts() {
        var model = new DefinedModel<>(TestDefinitionWithIgnore.class);
        var td = new TestDefinitionWithIgnore();
        model.load(td);

        model.set("name", "Test");
//        model.set("nested", new TestNestedDefinition());
//        model.set("nested.name", "Nested");

        assertThat(model.get("name"), is("Test"));
        assertThat(Arrays.stream(model.getDefinitions()).anyMatch(a->a.getName().equals("name")), is(true));
        assertThat(Arrays.stream(model.getDefinitions()).anyMatch(a->a.getName().equals("nested")), is(false));
//        assertThat(model.get("nested.name"), is("Nested"));
    }
}
