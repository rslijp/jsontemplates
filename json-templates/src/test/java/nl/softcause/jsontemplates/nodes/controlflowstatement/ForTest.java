package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.AssertionNode;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class ForTest {

    @Test
    public void should_collect_correct_arguments() {
        var forNode = new For();

        var argumentTypes = forNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Map.of(
                "start", new ArgumentDefinition(Types.OPTIONAL_INTEGER, 0L),
                "step", new ArgumentDefinition(Types.OPTIONAL_INTEGER, 1L),
                "until", new ArgumentDefinition(Types.INTEGER, null)
        )));
    }

    @Test
    public void should_collect_correct_slots() {
        var forNode = new For();

        var argumentTypes = forNode.getSlotTypes();

        assertThat(argumentTypes, is(Map.of(
                "bodyNode", new WildCardSlot())));
    }

    @Test
    public void should_execute_then_node_when_test_is_true() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var model = new TemplateModel<>(new TestDefinition());

        forNode.evaluate(model);

        assertThat(assertionNode.isCalled(), is(true));
        assertThat(assertionNode.getCounter(), is(3));
    }

    @Test
    public void should_execute_then_node_based_on_variables() {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Collections.singletonMap("until", new Variable("age")),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        TestDefinition testDefinition = new TestDefinition();
        testDefinition.setAge(3);
        var model = new TemplateModel<>(testDefinition);

        forNode.evaluate(model);

        assertThat(assertionNode.isCalled(), is(true));
        assertThat(assertionNode.getCounter(), is(3));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var assertionNode = new AssertionNode();
        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var json = new ObjectMapper().writeValueAsString(forNode);
        var obj = (INode) new ObjectMapper().readValue(json, INode.class);

        assertThat(forNode, is(obj));
    }

}
