package nl.softcause.jsontemplates.nodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.base.AllowedValues;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeException;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import nl.softcause.jsontemplates.types.AllowedValueSets;
import nl.softcause.jsontemplates.types.IAllowedValuesProvider;
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


    public static class AllowedValuesTestNodeWithBase extends AllowedValuesTestNode {

        public static AllowedValuesTestNodeWithBase create(String value) {
            var node = new AllowedValuesTestNodeWithBase();
            node.setArguments(Collections.singletonMap("value",new Constant(value)));
            node.setSlots(new HashMap<>());
            return node;
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

    @Test
    public void guard_should_accept_allowed_values_from_base_field(){
        var node = AllowedValuesTestNodeWithBase.create("Hello world");

        node.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(node.getOutput(), is("Hello world"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void guard_should_reject_not_allowed_values_from_base_field(){
        var node = AllowedValuesTestNodeWithBase.create("Hello world!");
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

    public static class LogParentNode extends
            ReflectionBasedNodeImpl {

        public static LogParentNode create(boolean error, INode body) {
            var node = new LogParentNode();
            node.setArguments(Collections.singletonMap("error", new Constant(error)));
            node.setSlots(Collections.singletonMap("body", new INode[]{body}));
            return node;
        }

//        public AllowedValuesTestNodeWithContext() {
//            super(AllowedValuesTestNodeWithContext.MyScope.class);
//        }

        @RequiredArgument
        private boolean error;

        @RequiredSlot
        private INode bodyNode = null;


        @Override
        public void internalEvaluate(TemplateModel model) {
//            var scope = pullScopeModel(model);
//            scope.error=error;
//            pushScopeModel(model,scope);
            bodyNode.evaluate(model);
        }
//
//        @SuppressWarnings("WeakerAccess")
//        public static class MyScope {
//            public boolean error = false;
//        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase("Test");
        }
    }

    public static class LogLevelProviderWithContext implements IAllowedValuesProvider {

        @Override
        public List<Object> valuesFor(String discriminator) {
            var set = allValues().
                    stream().
                    filter(v->v.match(discriminator)).
                    findFirst().
                    orElse( new AllowedValueSets( null, List.of("debug", "info", "warn", "error")));
            return set.getValues();
        }


        @Override
        public List<AllowedValueSets> allValues() {
            return List.of(
                    new AllowedValueSets( null, List.of("debug", "info", "warn", "error")),
                    new AllowedValueSets( "true", List.of( "warn", "error")),
                    new AllowedValueSets( "false", List.of("debug", "info")));
        }

    }


    public static class LogNode extends ReflectionBasedNodeImpl implements INodeWithParent<LogParentNode> {

        private LogParentNode parent;

        public static LogNode create(String level) {
            var node = new LogNode();
            node.setArguments(Collections.singletonMap("level",new Constant(level)));
            node.setSlots(new HashMap<>());
            return node;
        }

        @SuppressWarnings("unused")
        @AllowedValues(factory = LogLevelProviderWithContext.class, discriminatorField = "parent.error")
        private String level;
        @Getter
        private String output;


        @Override
        protected void internalEvaluate(TemplateModel model) {
            output=level;
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().add("Value is").expression(getArguments().get("value")).end();
        }

        @Override
        public void registerParent(LogParentNode parent) {
            this.parent=parent;
        }

        @Override
        public LogParentNode getRegisteredParent() {
            return this.parent;
        }
    }


    @Test
    public void guard_should_accept_allowed_values_from_parent_discriminator(){
        var logNode = LogNode.create("info");
        var node = LogParentNode.create(false, logNode);

        node.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(logNode.getOutput(), is("info"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void guard_should_reject_not_allowed_values_from_parent_discriminator(){
        var logNode = LogNode.create("info");
        var node = LogParentNode.create(true, logNode);
        try {
            node.evaluate(new TemplateModel<>(new TestDefinition()));
            fail();
        } catch(ReflectionBasedNodeException RBNe){
            assertThat(RBNe.getMessage(), is(
                    ReflectionBasedNodeException.
                            illegalValueFor("level", "info", new LogLevelProviderWithContext().valuesFor("true")).getMessage())
            );
        }
    }

}
