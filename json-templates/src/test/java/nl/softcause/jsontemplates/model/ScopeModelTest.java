package nl.softcause.jsontemplates.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class ScopeModelTest {

    @Test
    public void should_return_error_for_unknown_entry_on_requesting_definition() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.getDefinition("wrong");
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notFound("wrong").getMessage()));
        }
    }

    @Test
    public void should_return_definition_for_known_entry() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);
        //Case
        var def = model.getDefinition("known");

        assertThat(def, is(new DefinitionRegistryEntry("known", Types.INTEGER, null, true, true)));
    }

    @Test
    public void should_return_error_for_unknown_entry_on_requesting_value() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.get("wrong");
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notFound("wrong").getMessage()));
        }
    }

    @Test
    public void should_return_null_for_retrieving_known_not_set_optional_value() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OPTIONAL_INTEGER, null, true, true, null);

        //Case
        var value = model.get("known");

        //Then
        assertThat(value, nullValue());
    }


    @Test
    public void should_throw_an_error_for_non_optional_not_set_value() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.addDefintion("known", Types.INTEGER, null, true, true, null);
            fail();
        } catch (ScopeException Te) {
            //Then
            assertThat(Te.getMessage(),
                    is(ScopeException.defaultValueTypeError("known", Types.INTEGER, Types.NULL).getMessage()));
        }
    }

    @Test
    public void should_return_null_for_known_set_value() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OPTIONAL_INTEGER, null, true, true, null);
        model.set("known", 42);

        //Case
        var value = model.get("known");

        //Then
        assertThat(value, is(42L));
    }

    @Test
    public void should_return_error_for_unknown_entry_on_setting_value() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.set("wrong", 42);
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notFound("wrong").getMessage()));
        }
    }

    @Test
    public void should_return_error_for_non_writable_error() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OPTIONAL_INTEGER, null, true, false, null);

        //Case
        try {
            model.set("known", 42);
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notWritable("known").getMessage()));
        }
    }

    @Test
    public void should_return_error_for_non_readable_error() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OPTIONAL_INTEGER, null, false, true, null);

        //Case
        try {
            model.get("known");
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notReadable("known").getMessage()));
        }
    }

    @Test
    public void should_throw_an_error_for_setting_non_optional_value_to_null() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);

        //Case
        try {
            model.set("known", null);
            fail();
        } catch (TypeException Te) {
            //Then
            assertThat(Te.getMessage(), is(TypeException.invalidCast(null, Types.INTEGER).getMessage()));
        }
    }

    @Test
    public void should_report_double_entry() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);

        //Case
        try {
            model.addDefintion("known", Types.TEXT, null, true, true, 0);
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.alreadyDefined("known").getMessage()));
        }
    }

    @Test
    public void should_not_report_identical_entry() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);

        //When
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);

        assertThat(model.hasDefinition("known"), is(true));
    }

    @Test
    public void should_report_revoke_entry() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);
        model.getDefinition("known");

        //when
        model.dropDefintion("known");

        //Then
        try {
            model.getDefinition("known");
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notFound("known").getMessage()));
        }
    }

    @Test
    public void should_report_error_on_revoke_of_unknown_entry() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.dropDefintion("known");
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.notDefined("known").getMessage()));
        }
    }


    @Test
    public void should_not_allow_defining_nested_defintion() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.addDefintion("me.age", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true,
                    null);

            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.nestedDefinitionNotAllowed("me.age").getMessage()));
        }
    }

    @Test
    public void should_not_allow_removing_nested_defintion() {
        //Given
        var model = new ScopeModel(null);

        //Case
        try {
            model.dropDefintion("me.age");

            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.nestedDefinitionNotAllowed("me.age").getMessage()));
        }
    }

    @Test
    public void should_return_nested_definition_for_known_entry() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);

        //Case
        var def = model.getDefinition("known.magicNumbers");

        assertThat(def, is(new DefinitionRegistryEntry("magicNumbers", Types.LIST_INTEGER, null, true, true)));
    }

    @Test
    public void should_return_deep_nested_definition_for_known_entry() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);

        //Case
        var def = model.getDefinition("known.nested.name");

        //Then
        assertThat(def, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true)));
    }

    @Test
    public void should_not_allow_nesting_of_primitives() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);

        //Case
        try {
            model.dropDefintion("known.nested");

            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(), is(ScopeException.nestedDefinitionNotAllowed("known.nested").getMessage()));
        }
    }

    @Test
    public void should_return_nested_value() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);
        var td = new TestDefinition();
        td.setMentalAge(42);
        model.set("known", td);
        //Case
        var value = model.get("known.mentalAge");

        //Then
        assertThat(value, is(42L));
    }

    @Test
    public void should_return_deep_nested_value() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);
        var td = new TestDefinition();
        td.setMentalAge(42);
        var tnd = new TestNestedDefinition();
        tnd.setName("Hello world");
        td.setNested(tnd);
        model.set("known", td);
        //Case
        var value = model.get("known.nested.name");

        //Then
        assertThat(value, is("Hello world"));
    }

    @Test
    public void should_return_deep_nested_value_with_indexer() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);
        var td = new TestDefinition();
        td.setMentalAge(42);
        var tnd = new TestNestedDefinition();
        tnd.setName("Hello world");
        td.setChildren(new TestNestedDefinition[] {tnd});
        model.set("known", td);
        //Case
        var value = model.get("known.children[0].name");

        //Then
        assertThat(value, is("Hello world"));
    }

    @Test
    public void should_return_report_error_on_null() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);
        var td = new TestDefinition();
        model.set("known", td);
        //Case
        try {
            model.get("known.children[0].name");


            fail();
        } catch (ModelException Me) {
            //Then
            assertThat(Me.getMessage(), is(ModelException.nestedNullModel("children[0].name").getMessage()));
        }
    }

    @Test
    public void should_return_report_error_on_non_object() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.INTEGER, null, true, true, 0);
        model.set("known", 42);
        //Case
        try {
            model.get("known.children[0].name");
            fail();
        } catch (ScopeException Se) {
            //Then
            assertThat(Se.getMessage(),
                    is(ScopeException.nestedDefinitionNotAllowed("known.children[0].name").getMessage()));
        }
    }

    @Test
    public void should_set_nested_value() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);
        var td = new TestDefinition();
        model.set("known", td);

        //Case
        model.set("known.mentalAge", 42);

        //Then
        assertThat(td.getMentalAge(), is(42));
    }

    @Test
    public void should_set_deep_nested_value() {
        //Given
        var model = new ScopeModel(null);
        model.addDefintion("known", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, null);
        var td = new TestDefinition();
        td.setMentalAge(42);
        var tnd = new TestNestedDefinition();
        td.setNested(tnd);
        model.set("known", td);
        //Case

        model.set("known.nested.name", "Hello world");

        //Then
        assertThat(tnd.getName(), is("Hello world"));
    }

    @Test
    public void should_accept_matching_types() {
        var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));

        modelDefintion
                .addDefinition("rhs", Types.INTEGER, RegistryFactory.register(TestDefinition.class), true, true, 42);

        assertThat(modelDefintion.get("scope.rhs"), is(42L));

    }

    @Test
    public void should_reject_non_matching_default_value_definition() {
        var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));

        try {
            modelDefintion.addDefinition("rhs", Types.TEXT, null, true, true, 42);
            fail();
        } catch (ScopeException Se) {
            assertThat(Se.getMessage(),
                    is(ScopeException.defaultValueTypeError("rhs", Types.TEXT, Types.INTEGER).getMessage()));
        }

    }

    @Test
    public void should_accept_matching_object_types() {
        var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var td = new TestDefinition();
        modelDefintion
                .addDefinition("rhs", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, td);

        assertThat(modelDefintion.get("scope.rhs"), is(td));
    }

    @Test
    public void should_reject_non_matching_object_types() {
        var modelDefintion = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var td = new TestNestedDefinition();

        try {
            modelDefintion
                    .addDefinition("rhs", Types.OBJECT, RegistryFactory.register(TestDefinition.class), true, true, td);
            fail();
        } catch (ScopeException Se) {
            assertThat(Se.getMessage(),
                    is(ScopeException.defaultValueTypeError("rhs", TestDefinition.class, TestNestedDefinition.class)
                            .getMessage()));
        }
    }

}
