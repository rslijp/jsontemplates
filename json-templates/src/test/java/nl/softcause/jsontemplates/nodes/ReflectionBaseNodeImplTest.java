package nl.softcause.jsontemplates.nodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.base.AllowedValues;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeException;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import nl.softcause.jsontemplates.types.StaticValuesProvider;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

    public static class AllowedValuesProvider extends StaticValuesProvider {


        @SuppressWarnings({"unchecked", "WeakerAccess"})
        public AllowedValuesProvider() {
            super("Hello world","Goodbye");
        }
    }
    public static class AllowedValuesTestNode extends ReflectionBasedNodeImpl {

        public static AllowedValuesTestNode create(String value) {
            var node = new AllowedValuesTestNode();
                node.setArguments(Collections.singletonMap("value",new Constant(value)));
                node.setSlots(new HashMap<>());
            return node;
        }

        @SuppressWarnings("unused")
        @AllowedValues(factory = AllowedValuesProvider.class)
        private String value;
        @Getter
        private String output;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            output=value;
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().add("Value is").expression(getArguments().get("value")).end();
        }
    }


    @Test
    public void guard_should_accept_allowed_values(){
        var node = AllowedValuesTestNode.create("Hello world");

        node.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(node.getOutput(), is("Hello world"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void guard_should_reject_not_allowed_values(){
        var node = AllowedValuesTestNode.create("Hello world!");
        try {
            node.evaluate(new TemplateModel<>(new TestDefinition()));
            fail();
        } catch(ReflectionBasedNodeException RBNe){
            assertThat(RBNe.getMessage(), is(
                    ReflectionBasedNodeException.
                            illegalValueFor("value", "Hello world!", new AllowedValuesProvider().valuesFor(null)).getMessage())
            );
        }
    }
}
