package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.Add;
import nl.softcause.jsontemplates.expressions.comparison.LessThan;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.AssertionNode;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class WhileTest {

    @Test
    public void should_collect_correct_arguments() {
        var whileNode = new While();

        var argumentTypes = whileNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Collections.singletonMap("test", new ArgumentDefinition(Types.BOOLEAN, false))));
    }

    @Test
    public void should_collect_correct_slots() {
        var whileNode = new While();

        var argumentTypes = whileNode.getSlotTypes();

        assertThat(argumentTypes, is(Map.of(
                "bodyNode", new WildCardSlot())));
    }

    @Test
    public void should_execute_then_node_when_test_is_true() {
        var lt = new LessThan();
        lt.setArguments(Arrays.asList(new Variable("age"), new Constant(3)));
        var add = new Add();
        add.setArguments(Arrays.asList(new Variable("age"), new Constant(1)));
        var assertionNode = Set.create(
                Map.of("path", new Constant("age"), "value", add)
        );
        var forNode = While.create(
                Collections.singletonMap("test", lt),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var model = new TestDefinition();
        forNode.evaluate(new TemplateModel<>(model));

        assertThat(model.getAge(), is(3));
    }

    @Test
    public void should_execute_then_node_when_test_is_true_XXX() {
        var lt = new LessThan();
        lt.setArguments(Arrays.asList(new Variable("age"), new Constant(3)));
        var add = new Add();
        add.setArguments(Arrays.asList(new Variable("age"), new Constant(1)));
        var assertionNode = Set.create(
                Map.of("path", new Constant("age"), "value", add)
        );
        var forNode = While.create(
                Collections.singletonMap("test", lt),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );
        var model = new TestDefinition();
        forNode.evaluate(new TemplateModel<>(model));

        assertThat(model.getAge(), is(3));
    }


    @Test
    public void should_serialize_to_json() throws IOException {
        var assertionNode = new AssertionNode();
        var forNode = While.create(
                Collections.singletonMap("test", new Constant(false)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var json = new ObjectMapper().writeValueAsString(forNode);

        var obj = new ObjectMapper().readValue(json, INode.class);

        assertThat(forNode, is(obj));
    }

}
