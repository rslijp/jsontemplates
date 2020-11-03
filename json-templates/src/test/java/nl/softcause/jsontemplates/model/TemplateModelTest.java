package nl.softcause.jsontemplates.model;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class TemplateModelTest {

    @Test
    public void Should_retrieve_model_property_definition() {
        var model = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));

        var d = model.getDefinition("name");

        assertThat(d, is(new DefinitionRegistryEntry("name", Types.OPTIONAL_TEXT, null, true, true, null)));
    }

    @Test
    public void Should_retrieve_model_property_value() {
        var td = new TestDefinition();
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        td.setName("Hello world");
        model.load(td);

        var d = model.get("name");

        assertThat(d, is("Hello world"));
    }

    @Test
    public void Should_retrieve_scope_property_value() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Hello world");

        var d = model.get("scope.name");

        assertThat(d, is("Hello world"));
    }

    @Test
    public void Should_retrieve_scope_property_value_and_auto_resolve_parents() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Hello world");
        model.pushScope(null);

        var d = model.get("scope.name");

        assertThat(d, is("Hello world"));
    }


    @Test
    public void Should_retrieve_scope_property_value_from_explicit_parent_scope() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Hello world");
        model.pushScope(null);

        var d = model.get("parent.scope.name");

        assertThat(d, is("Hello world"));
    }

    @Test
    public void Should_retrieve_scope_property_value_from_explicit_parent_scope_2() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Hello world");
        model.pushScope(null);
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Guten Abend");
        model.pushScope(null);

        assertThat(model.get("parent.parent.scope.name"), is("Hello world"));
        assertThat(model.get("parent.scope.name"), is("Guten Abend"));
    }

    @Test
    public void Should_disable_auto_parent_when_parent_is_defined() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Hello world");
        model.pushScope(null);
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.scope().set("name", "Guten Abend");
        model.pushScope(null);
        model.pushScope(null);

        try {
            model.get("parent.scope.name");
            fail();
        } catch (ScopeException Se) {
            assertThat(Se.getMessage(), is(ScopeException.notFound("name").getMessage()));
        }
    }

    //setter tests
    @Test
    public void Should_write_model_property_value() {
        var td = new TestDefinition();
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.load(td);

        model.set("name", "Hello world");

        assertThat(td.getName(), is("Hello world"));
    }

    @Test
    public void Should_write_nested_model_property_value() {
        var td = new TestDefinition();
        td.setNested(new TestNestedDefinition());
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.load(td);

        model.set("nested.name", "Hello world");

        assertThat(td.getNested().getName(), is("Hello world"));
    }

    @Test
    public void Should_write_scope_property_value() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);

        model.set("scope.name", "Hello world");

        assertThat(model.scope().get("name"), is("Hello world"));
    }

    @Test
    public void Should_not_resolve_scope_property_value() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.pushScope(null);

        try {
            model.set("scope.name", "Hello world");
        } catch (ScopeException Se) {
            assertThat(Se.getMessage(), is(ScopeException.notFound("name").getMessage()));
        }
    }


    @Test
    public void Should_not_write_scope_property_value_from_explicit_parent_scope() {
        var model = new TemplateModel<>(new DefinedModel<>(new TestDefinition()));
        model.addDefinition("name", Types.OPTIONAL_TEXT, null, true, true, null, null);
        model.pushScope(null);

        try {
            model.set("parent.scope.name", "Hello world");
        } catch (ScopeException Se) {
            assertThat(Se.getMessage(),
                    is(ScopeException.writingInParentScopesNotAllowed("parent.scope.name").getMessage()));
        }
    }


}
