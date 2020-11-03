package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.AssertionNode;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class IfTest {

    @Test
    public void should_collect_correct_arguments() {
        var ifNode = new If();

        var argumentTypes = ifNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Collections.singletonMap("test", new ArgumentDefinition(Types.BOOLEAN, false))));
    }

    @Test
    public void should_collect_correct_slots() {
        var ifNode = new If();

        var argumentTypes = ifNode.getSlotTypes();
        assertThat(argumentTypes, is(Map.of(
                "thenNode", new WildCardSlot(),
                "elseNode", new OptionalSlot(new WildCardSlot()))));
    }

    @Test
    public void should_retain_order_of_slots() {
        var ifNode = new If();

        var list = new ArrayList<>(ifNode.getSlotTypes().entrySet());
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getKey(), is("thenNode"));
        assertThat(list.get(1).getKey(), is("elseNode"));

    }

    @Test
    public void should_execute_then_node_when_test_is_true() {
        var assertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Collections.singletonMap("then", new INode[] {assertionNode})
        );

        ifNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNode.isCalled(), is(true));
    }

    @Test
    public void should_execute_then_node_when_test_is_true_but_not_else_node() {
        var thenAssertionNode = new AssertionNode();
        var elseAssertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("then", new INode[] {thenAssertionNode},
                        "else", new INode[] {elseAssertionNode})
        );


        ifNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(thenAssertionNode.isCalled(), is(true));
        assertThat(elseAssertionNode.isCalled(), is(false));
    }

    @Test
    public void should_execute_else_node_when_test_is_true_but_not_then_node() {
        var thenAssertionNode = new AssertionNode();
        var elseAssertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("then", new INode[] {thenAssertionNode},
                        "else", new INode[] {elseAssertionNode})
        );


        ifNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(thenAssertionNode.isCalled(), is(false));
        assertThat(elseAssertionNode.isCalled(), is(true));
    }


    @Test
    public void should_acccept_empty_else_node() {
        var thenAssertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("then", new INode[] {thenAssertionNode})
        );


        ifNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(thenAssertionNode.isCalled(), is(false));
    }

    @Test
    public void should_apply_model() {
        var thenAssertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Variable("scope.test")),
                Map.of("then", new INode[] {thenAssertionNode})
        );

        var model = new TemplateModel<>(new TestDefinition());
        model.scope().addDefintion("test", Types.BOOLEAN, null, true, false, true, null);
        ifNode.evaluate(model);

        assertThat(thenAssertionNode.isCalled(), is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var thenAssertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Variable("scope.test")),
                Map.of("then", new INode[] {thenAssertionNode})
        );


        var json = new ObjectMapper().writeValueAsString(ifNode);

        var obj = new ObjectMapper().readValue(json, INode.class);

        assertThat(ifNode, is(obj));
    }
}
