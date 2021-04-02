package nl.softcause.jsontemplates.definition;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.model.DefinedModel;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.ReflectionBaseNodeImplTest;
import nl.softcause.jsontemplates.nodes.base.AllowedValues;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.types.StaticValuesProvider;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

public class DescribeTemplateLibraryTest {

    @Test
    public void should_serialize() throws IOException {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);
        var json = new ObjectMapper().writeValueAsString(description);

        System.out.println(json.length());
        assertThat(json.isEmpty(), is(false));
    }

    @Test
    public void should_describe_infix_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt =
                description.getExpressionDescriptions().stream().filter(f -> f.getOperator().equals("+")).findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("Add"));
        assertThat(expDef.getOperator(), is("+"));
        assertThat(expDef.getArgumentTypes(), is(Arrays.asList("decimal", "decimal")));
        assertThat(expDef.getPriority(), is(8));
        assertThat(expDef.getReturnType(), is("decimal"));
        assertThat(expDef.getParseType(), is("INFIX"));
        assertThat(expDef.isSpecial(), is(false));
    }

    @Test
    public void should_describe_unary_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt =
                description.getExpressionDescriptions().stream().filter(f -> f.getOperator().equals("!")).findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("Not"));
        assertThat(expDef.getOperator(), is("!"));
        assertThat(expDef.getArgumentTypes(), is(Collections.singletonList("boolean")));
        assertThat(expDef.getPriority(), is(8));
        assertThat(expDef.getReturnType(), is("boolean"));
        assertThat(expDef.getParseType(), is("UNARY"));
        assertThat(expDef.isSpecial(), is(false));
    }

    @Test
    public void should_describe_function_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt =
                description.getExpressionDescriptions().stream().filter(f -> f.getName().equals("Concat")).findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("Concat"));
        assertThat(expDef.getOperator(), is("concat"));
        assertThat(expDef.getArgumentTypes(), is(Arrays.asList("text?", "text?")));
        assertThat(expDef.getPriority(), is(1));
        assertThat(expDef.getReturnType(), is("text"));
        assertThat(expDef.getParseType(), is("FUNCTION"));
        assertThat(expDef.isSpecial(), is(false));
    }


    @Test
    public void should_describe_variable_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f -> f.getName().equals("variable"))
                .findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("variable"));
        assertThat(expDef.getOperator(), is("variable"));
        assertThat(expDef.getArgumentTypes(), nullValue());
        assertThat(expDef.getPriority(), nullValue());
        assertThat(expDef.getReturnType(), nullValue());
        assertThat(expDef.getParseType(), nullValue());
        assertThat(expDef.isSpecial(), is(true));
    }

    @Test
    public void should_describe_constant_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f -> f.getName().equals("constant"))
                .findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("constant"));
        assertThat(expDef.getOperator(), is("constant"));
        assertThat(expDef.getArgumentTypes(), nullValue());
        assertThat(expDef.getPriority(), nullValue());
        assertThat(expDef.getReturnType(), nullValue());
        assertThat(expDef.getParseType(), nullValue());
        assertThat(expDef.isSpecial(), is(true));
    }

    @Test
    public void should_describe_brackets_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f -> f.getName().equals("Brackets"))
                .findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("Brackets"));
        assertThat(expDef.getOperator(), is("brackets"));
        assertThat(expDef.getArgumentTypes(), is(Collections.singletonList("T")));
        assertThat(expDef.getPriority(), is(12));
        assertThat(expDef.getReturnType(), is("T"));
        assertThat(expDef.getParseType(), is("BRACKETS"));
        assertThat(expDef.isSpecial(), is(false));
    }

    @Test
    public void should_describe_ternary_expression() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt =
                description.getExpressionDescriptions().stream().filter(f -> f.getName().equals("Ternary")).findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("Ternary"));
        assertThat(expDef.getOperator(), is("ternary"));
        assertThat(expDef.getArgumentTypes(), is(Arrays.asList("boolean", "T", "T")));
        assertThat(expDef.getPriority(), is(3));
        assertThat(expDef.getReturnType(), is("T"));
        assertThat(expDef.getParseType(), is("TERNARY"));
        assertThat(expDef.isSpecial(), is(false));
    }

    @Test
    public void should_describe_simple_node_no_scope() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt = description.getNodeDescriptions().stream().filter(f -> f.getName().equals("Set")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("Set"));
        assertThat(nodeDef.getNodeSlots(), nullValue());
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        var slots = new HashMap<String, String>();
        slots.put("path", "text");
        slots.put("value", "object");
        assertThat(nodeDef.getArgumentTypes(), is(slots));
        assertThat(nodeDef.getScopeChanges(), nullValue());
    }

    @Test
    public void should_describe_simple_node_with_default_scope() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt =
                description.getNodeDescriptions().stream().filter(f -> f.getName().equals("While")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("While"));
        assertThat(nodeDef.getNodeSlots(), is(Collections.singletonMap("bodyNode", "*")));
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), is(Collections.singletonMap("test", "boolean")));
        assertThat(nodeDef.getScopeChanges(), is(Collections.emptyMap()));
    }

    @Test
    public void should_retain_order_insize_description() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt =
                description.getNodeDescriptions().stream().filter(f -> f.getName().equals("If")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("If"));
        var list = new ArrayList<>(nodeDef.getNodeSlots().entrySet());
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getKey(), is("thenNode"));
        assertThat(list.get(1).getKey(), is("elseNode"));

    }

    public static class AllowedValuesProvider extends StaticValuesProvider {

        @SuppressWarnings({"unchecked", "WeakerAccess"})
        public AllowedValuesProvider() {
            super("Hello world","Goodbye");
        }
    }

    public static class AllowedValuesTestNode extends ReflectionBasedNodeImpl {

        public static ReflectionBaseNodeImplTest.AllowedValuesTestNode create(String value) {
            var node = new ReflectionBaseNodeImplTest.AllowedValuesTestNode();
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
    public void should_describe_simple_node_with_allowed_values() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().addMainNodes(AllowedValuesTestNode.class).describe(modelDefinition);

        var nodeDefOpt =
                description.getNodeDescriptions().stream().filter(f -> f.getName().equals("AllowedValuesTestNode")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("AllowedValuesTestNode"));
        assertThat(nodeDef.getNodeSlots(), nullValue());
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), is(Map.of(
                "value", "text?",
                "output", "text?"
        )));
        assertThat(nodeDef.getAllowedValues(), is(Map.of(
                "value", new AllowedValuesDescription("","", new AllowedValuesProvider().allValues())
        )));
//        assertThat(nodeDef.getScopeChanges(), is(Collections.emptyMap()));
    }

    @Test
    public void should_describe_simple_node_with_allowed_values_even_after_serialization() throws
            JsonProcessingException {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var original = new DescribeTemplateLibrary().addMainNodes(AllowedValuesTestNode.class);

        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(original);
        System.out.println(json);
        var reconstructed = mapper.readValue(json, DescribeTemplateLibrary.class);
        var description = reconstructed.describe(modelDefinition);

        var nodeDefOpt =
                description.getNodeDescriptions().stream().filter(f -> f.getName().equals("AllowedValuesTestNode")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("AllowedValuesTestNode"));
        assertThat(nodeDef.getNodeSlots(), nullValue());
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), is(Map.of(
                "value", "text?",
                "output", "text?"
        )));
        assertThat(nodeDef.getAllowedValues(), is(Map.of(
                "value", new AllowedValuesDescription("","", new AllowedValuesProvider().allValues())
        )));
//        assertThat(nodeDef.getScopeChanges(), is(Collections.emptyMap()));
    }


    public static class AllowedValuesTestNodeWithBase extends AllowedValuesTestNode {

    }

    @Test
    public void should_describe_simple_node_with_allowed_values_with_base() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().addMainNodes(AllowedValuesTestNodeWithBase.class).describe(modelDefinition);

        var nodeDefOpt =
                description.getNodeDescriptions().stream().filter(f -> f.getName().equals("AllowedValuesTestNodeWithBase")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("AllowedValuesTestNodeWithBase"));
        assertThat(nodeDef.getNodeSlots(), nullValue());
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), is(Map.of(
                "value", "text?",
                "output", "text?"
        )));
        assertThat(nodeDef.getAllowedValues(), is(Map.of(
                "value", new AllowedValuesDescription("","", new AllowedValuesProvider().allValues())
        )));
//        assertThat(nodeDef.getScopeChanges(), is(Collections.emptyMap()));
    }


    @Test
    public void should_describe_node_with_scope_change() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt = description.getNodeDescriptions().stream().filter(f -> f.getName().equals("Try")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("Try"));
        var slots = new HashMap<String, String>();
        slots.put("bodyNode", "*");
        slots.put("onErrorNode", "*?");
        assertThat(nodeDef.getNodeSlots(), is(slots));
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), nullValue());
        var scope = new HashMap<String, NodeScopeDescription>();
        scope.put("exception", new NodeScopeDescription(Types.OBJECT.getType(), false));
        scope.put("errorMessage", new NodeScopeDescription(Types.OPTIONAL_TEXT.getType(), false));
        assertThat(nodeDef.getScopeChanges(), is(scope));
    }

    @Test
    public void should_describe_node_with_slot_limits() {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt =
                description.getNodeDescriptions().stream().filter(f -> f.getName().equals("Switch")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("Switch"));
        var slots = new HashMap<String, String>();
        slots.put("caseNode", "limited");
        slots.put("defaultNode", "*?");
        assertThat(nodeDef.getNodeSlots(), is(slots));
        var caseId = description.getNodeDescriptions().stream().filter(f -> f.getName().equals("Case")).findFirst()
                .orElseThrow().getId();
        assertThat(nodeDef.getNodeSlotLimits().size(), is(1));
        assertThat(nodeDef.getNodeSlotLimits().keySet(), hasItem("caseNode"));
        assertThat(nodeDef.getNodeSlotLimits().get("caseNode"), is(new long[] {caseId}));
        assertThat(nodeDef.getArgumentTypes(), nullValue());
        assertThat(nodeDef.getScopeChanges(),
                is(Collections.singletonMap("picked", new NodeScopeDescription(Types.BOOLEAN.getType(), false))));
    }


    @Test
    public void should_describe_main_model() {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description.getModelDescriptions();
        var mainModelId = model.stream()
                .filter(d -> d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst().orElseThrow().getId();

        assertThat(description.getMainModelId(), is(mainModelId));
    }

    @Test
    public void should_describe_model_and_nested_model() {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description.getModelDescriptions();

        assertThat(model.size(), is(2));
        assertThat(model.stream().map(d -> d.getType()).collect(Collectors.toList()),
                hasItem("nl.softcause.jsontemplates.definition.TemplateDescription"));
        assertThat(model.stream().map(d -> d.getType()).collect(Collectors.toList()),
                hasItem("nl.softcause.jsontemplates.model.TestNestedDefinition"));
    }

    @Test
    public void should_describe_properties() {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description
                .getModelDescriptions()
                .stream()
                .filter(d -> d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst()
                .orElseThrow();

        assertThat(model.getPropertyDescriptions().size(), is(13));
        assertThat(model.getPropertyDescriptions().stream().map(c -> c.getName()).collect(Collectors.toList()),
                hasItems(
                        "name", "nameGet", "nameSet", "age", "mentalAge", "magicNumbers", "titles", "certificates",
                        "nested", "children", "other", "map", "moreMagic"
                ));
    }

    @Test
    public void should_describe_model_property() {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description
                .getModelDescriptions()
                .stream()
                .filter(d -> d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst()
                .orElseThrow();

        var property = model.getPropertyDescriptions().stream().filter(c -> c.getName().equals("name")).findFirst()
                .orElseThrow();

        assertThat(property.getModelReference(), nullValue());
        assertThat(property.getName(), is("name"));
        assertThat(property.getType(), is("text?"));
        assertThat(property.isWritable(), is(true));
        assertThat(property.isReadable(), is(true));
    }

    @Test
    public void should_describe_model_property_with_model_reference() {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description
                .getModelDescriptions()
                .stream()
                .filter(d -> d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst()
                .orElseThrow();

        var nestedModelId = description
                .getModelDescriptions()
                .stream()
                .filter(d -> d.getType().equals("nl.softcause.jsontemplates.model.TestNestedDefinition"))
                .findFirst()
                .orElseThrow().getId();

        var property = model.getPropertyDescriptions().stream().filter(c -> c.getName().equals("children")).findFirst()
                .orElseThrow();

        assertThat(property.getModelReference(), is(nestedModelId));
        assertThat(property.getName(), is("children"));
        assertThat(property.getType(), is("object*"));
        assertThat(property.isWritable(), is(true));
        assertThat(property.isReadable(), is(true));
    }
}

