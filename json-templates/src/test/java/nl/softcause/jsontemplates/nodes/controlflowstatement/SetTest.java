package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.INode;
import org.junit.Test;

public class SetTest {


    @Test
    public void should_set_value_on_model() {
        var setNode = Set.create(
                Map.of("path", new Constant("name"),
                        "value", new Constant("Hello world"))
        );

        var model = new TestDefinition();
        setNode.evaluate(new TemplateModel<>(model));

        assertThat(model.getName(), is("Hello world"));
    }


    @Test
    public void should_set_value_on_scope() {
        var setNode = Set.create(
                Map.of("path", new Constant("scope.name"),
                        "value", new Constant("Hello world"))
        );

        var model = new TemplateModel<>(new TestDefinition());
        setNode.evaluate(model);

        assertThat(model.scope().get("name"), is("Hello world"));
    }

    @Test
    public void should_set_value_on_scope_with_variable() {
        var setNode = Set.create(
                Map.of("path", new Constant("scope.greets"),
                        "value", new Variable("name"))
        );

        var def = new TestDefinition();
        def.setName("Hello world");
        var model = new TemplateModel<>(def);
        setNode.evaluate(model);

        assertThat(model.scope().get("greets"), is("Hello world"));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var setNode = Set.create(
                Map.of("path", new Constant("age"),
                        "value", new Variable("scope.current"))
        );


        var json = new ObjectMapper().writeValueAsString(setNode);
        var obj = new ObjectMapper().readValue(json, INode.class);

        assertThat(setNode, is(obj));
    }

}
