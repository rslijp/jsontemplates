package nl.softcause.jsontemplates.definition;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.model.*;
import nl.softcause.jsontemplates.types.Types;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
    public void should_describe_infix_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getOperator().equals("+")).findFirst();
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
    public void should_describe_unary_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getOperator().equals("!")).findFirst();
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
    public void should_describe_function_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getName().equals("Concat")).findFirst();
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
    public void should_describe_variable_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getName().equals("variable")).findFirst();
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
    public void should_describe_constant_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getName().equals("constant")).findFirst();
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
    public void should_describe_brackets_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getName().equals("Brackets")).findFirst();
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
    public void should_describe_ternary_expression()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var expDefOpt = description.getExpressionDescriptions().stream().filter(f->f.getName().equals("Ternary")).findFirst();
        assertThat(expDefOpt.isPresent(), is(true));
        var expDef = expDefOpt.orElseThrow();
        assertThat(expDef.getName(), is("Ternary"));
        assertThat(expDef.getOperator(), is("ternary"));
        assertThat(expDef.getArgumentTypes(), is(Arrays.asList("boolean","T","T")));
        assertThat(expDef.getPriority(), is(3));
        assertThat(expDef.getReturnType(), is("T"));
        assertThat(expDef.getParseType(), is("TERNARY"));
        assertThat(expDef.isSpecial(), is(false));
    }

    @Test
    public void should_describe_simple_node_no_scope()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Set")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("Set"));
        assertThat(nodeDef.getNodeSlots(), nullValue());
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        var slots = new HashMap<String,String>();
        slots.put("path","text");
        slots.put("value","object");
        assertThat(nodeDef.getArgumentTypes(), is(slots));
        assertThat(nodeDef.getScopeChanges(), nullValue());
    }

    @Test
    public void should_describe_simple_node_with_default_scope()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt = description.getNodeDescriptions().stream().filter(f->f.getName().equals("While")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("While"));
        assertThat(nodeDef.getNodeSlots(), is(Collections.singletonMap("bodyNode","*")));
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), is(Collections.singletonMap("test","boolean")));
        assertThat(nodeDef.getScopeChanges(), is(Collections.emptyMap()));
    }

    @Test
    public void should_describe_node_with_scope_change()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Try")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("Try"));
        var slots = new HashMap<String,String>();
        slots.put("bodyNode","*");
        slots.put("onErrorNode","*?");
        assertThat(nodeDef.getNodeSlots(), is(slots));
        assertThat(nodeDef.getNodeSlotLimits(), nullValue());
        assertThat(nodeDef.getArgumentTypes(), nullValue());
        var scope = new HashMap<String,NodeScopeDescription>();
        scope.put("exception", new NodeScopeDescription(Types.OBJECT.getType(), false));
        scope.put("errorMessage", new NodeScopeDescription(Types.OPTIONAL_TEXT.getType(), false));
        assertThat(nodeDef.getScopeChanges(), is(scope));
    }

    @Test
    public void should_describe_node_with_slot_limits()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var nodeDefOpt = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Switch")).findFirst();
        assertThat(nodeDefOpt.isPresent(), is(true));
        var nodeDef = nodeDefOpt.orElseThrow();
        assertThat(nodeDef.getName(), is("Switch"));
        var slots = new HashMap<String,String>();
        slots.put("caseNode","limited");
        slots.put("defaultNode","*?");
        assertThat(nodeDef.getNodeSlots(), is(slots));
        var caseId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Case")).findFirst().orElseThrow().getId();
        assertThat(nodeDef.getNodeSlotLimits().size(), is(1));
        assertThat(nodeDef.getNodeSlotLimits().keySet(), hasItem("caseNode"));
        assertThat(nodeDef.getNodeSlotLimits().get("caseNode"), is(new int[]{caseId}));
        assertThat(nodeDef.getArgumentTypes(), nullValue());
        assertThat(nodeDef.getScopeChanges(), is(Collections.singletonMap("picked",new NodeScopeDescription(Types.BOOLEAN.getType(), false))));
    }

    @Test
    public void should_describe_main_nodes()  {
        var modelDefinition = new TemplateModel<>(new DefinedModel<>(TestDefinition.class));
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var forId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("For")).findFirst().orElseThrow().getId();
        var ifId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("If")).findFirst().orElseThrow().getId();
        var setId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Set")).findFirst().orElseThrow().getId();
        var switchId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Switch")).findFirst().orElseThrow().getId();
        var tryId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("Try")).findFirst().orElseThrow().getId();
        var whileId = description.getNodeDescriptions().stream().filter(f->f.getName().equals("While")).findFirst().orElseThrow().getId();

        assertThat(description.getMainNodeIds().size(), is(6));
        assertThat(description.getMainNodeIds(), hasItems(forId,ifId,setId,switchId,tryId,whileId));
    }

    @Test
    public void should_describe_main_model()  {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description.getModelDescriptions();
        var mainModelId=model.stream().filter(d->d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription")).findFirst().orElseThrow().getId();

        assertThat(description.getMainModelId(), is(mainModelId));
    }

    @Test
    public void should_describe_model_and_nested_model()  {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description.getModelDescriptions();

        assertThat(model.size(), is(2));
        assertThat(model.stream().map(d->d.getType()).collect(Collectors.toList()), hasItem("nl.softcause.jsontemplates.definition.TemplateDescription"));
        assertThat(model.stream().map(d->d.getType()).collect(Collectors.toList()), hasItem("nl.softcause.jsontemplates.model.TestNestedDefinition"));
    }

    @Test
    public void should_describe_properties()  {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description
                .getModelDescriptions()
                .stream()
                .filter(d->d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst()
                .orElseThrow();

        assertThat(model.getPropertyDescriptions().size(), is(13));
        assertThat(model.getPropertyDescriptions().stream().map(c->c.getName()).collect(Collectors.toList()), hasItems(
                "name","nameGet","nameSet","age","mentalAge","magicNumbers","titles","certificates","nested","children","other","map","moreMagic"
        ));
    }

    @Test
    public void should_describe_model_property()  {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description
                .getModelDescriptions()
                .stream()
                .filter(d->d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst()
                .orElseThrow();

        var property = model.getPropertyDescriptions().stream().filter(c->c.getName().equals("name")).findFirst().orElseThrow();

        assertThat(property.getModelReference(), nullValue());
        assertThat(property.getName(), is("name"));
        assertThat(property.getType(), is("text?"));
        assertThat(property.isWritable(), is(true));
        assertThat(property.isReadable(), is(true));
    }

    @Test
    public void should_describe_model_property_with_model_reference()  {
        DefinedModel<TestDefinition> definition = new DefinedModel<>(TestDefinition.class);
        var modelDefinition = new TemplateModel<>(definition);
        var description = new DescribeTemplateLibrary().describe(modelDefinition);

        var model = description
                .getModelDescriptions()
                .stream()
                .filter(d->d.getType().equals("nl.softcause.jsontemplates.definition.TemplateDescription"))
                .findFirst()
                .orElseThrow();

        var nestedModelId = description
                .getModelDescriptions()
                .stream()
                .filter(d->d.getType().equals("nl.softcause.jsontemplates.model.TestNestedDefinition"))
                .findFirst()
                .orElseThrow().getId();

        var property = model.getPropertyDescriptions().stream().filter(c->c.getName().equals("children")).findFirst().orElseThrow();

        assertThat(property.getModelReference(), is(nestedModelId));
        assertThat(property.getName(), is("children"));
        assertThat(property.getType(), is("object*"));
        assertThat(property.isWritable(), is(true));
        assertThat(property.isReadable(), is(true));
    }
}

