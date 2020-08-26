package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static nl.softcause.jsontemplates.nodes.controlflowstatement.TestBuilderTool.node;
import static nl.softcause.jsontemplates.nodes.controlflowstatement.TestBuilderTool.set;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.Add;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.IntScopeSlotNode;
import nl.softcause.jsontemplates.nodes.base.MultiNode;
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
    public void should_set_int_value_on_tag_scope() {
        var setNode = node(
                set("scope.x", "30"),
                set("scope.x", "$scope.x+7"),
                IntScopeSlotNode.create("age",
                        set("scope.value", "$scope.x"))

            );

        var m = new TestDefinition();
        var model = new TemplateModel<>(m);
        setNode.evaluate(model);

        assertThat(m.getAge(), is(37));
    }

    @Test
    public void should_update_value_on_scope() {
        var setNode = new MultiNode(
                new INode[]{
                    Set.create(
                         Map.of("path", new Constant("scope.count"),
                                "value", new Constant(1))
                    ),
                    Set.create(
                            Map.of("path", new Constant("scope.count"),
                                    "value", new ExpressionParser().parse("$scope.count+2"))
                    )
                }
        );

        var model = new TemplateModel<>(new TestDefinition());
        setNode.evaluate(model);

        assertThat(model.scope().get("count"), is(3.0));
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
