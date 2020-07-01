package nl.softcause.jsontemplates.nodes;

import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionBaseNodeImplTest {

    @Test
    public void should_collect_correct_slots() {
        var ifNode = new If();

        var argumentTypes = ifNode.getSlotTypes();

        assertThat(argumentTypes, is(Map.of(
                "thenNode", new WildCardSlot(),
                "elseNode", new OptionalSlot(new WildCardSlot()))));
    }

    public static class IfExtension extends If {
        public static IfExtension create(Map<String, IExpression> arguments, Map<String, INode[]> slots) {
            var ifNode = new IfExtension();
            ifNode.setArguments(arguments);
            ifNode.setSlots(slots);
            return ifNode;
        }
    }


    @Test
    public void should_collect_correct_slots_from_base() {
        var ifNode = new IfExtension();

        var argumentTypes = ifNode.getSlotTypes();

        assertThat(argumentTypes, is(Map.of(
                "thenNode", new WildCardSlot(),
                "elseNode", new OptionalSlot(new WildCardSlot()))));
    }

    @Test
    public void should_execute_node() {
        var assertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Collections.singletonMap("then", new INode[] {assertionNode})
        );

        ifNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNode.isCalled(), is(true));
    }

    @Test
    public void should_execute_node_from_base() {
        var assertionNode = new AssertionNode();
        var ifNode = IfExtension.create(
                Collections.singletonMap("test", new Constant(true)),
                Collections.singletonMap("then", new INode[] {assertionNode})
        );

        ifNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNode.isCalled(), is(true));
    }
}
