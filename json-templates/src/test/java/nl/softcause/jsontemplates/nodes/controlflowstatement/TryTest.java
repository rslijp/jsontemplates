package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.AssertionNode;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import org.junit.Test;

public class TryTest {

    @Test
    public void should_collect_correct_arguments() {
        var tryNode = new Try();

        var argumentTypes = tryNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Collections.emptyMap()));
    }

    @Test
    public void should_collect_correct_slots() {
        var tryNode = new Try();

        var argumentTypes = tryNode.getSlotTypes();

        assertThat(argumentTypes, is(Map.of(
                "bodyNode", new WildCardSlot(),
                "onErrorNode", new OptionalSlot(new WildCardSlot()))));
    }

    @Test
    public void should_not_execute_errorNode_when_no_error() {
        var regularNode = new AssertionNode();
        var errorNode = new AssertionNode();
        var tryNode = Try.create(
                Map.of("body", new INode[] {regularNode},
                        "onError", new INode[] {errorNode})
        );

        tryNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(regularNode.isCalled(), is(true));
    }

    @Test
    public void should_execute_errorNode_when_errors() {
        var regularNode = new AssertionNode().throwErrorOnEvaluate("Oops");
        var errorNode = new AssertionNode().validate(
                model -> {
                    assertThat(model.scope().get("errorMessage"), is("Oops"));
                    assertThat(model.scope().get("exception"), notNullValue());
                    return null;
                }
        );
        var tryNode = Try.create(
                Map.of("body", new INode[] {regularNode}, "onError", new INode[] {errorNode})
        );

        tryNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(regularNode.isCalled(), is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var regularNode = new AssertionNode();
        var errorNode = new AssertionNode();
        var tryNode = Try.create(
                Map.of("body", new INode[] {regularNode}, "onError", new INode[] {errorNode})
        );

        var json = new ObjectMapper().writeValueAsString(tryNode);

        var obj = new ObjectMapper().readValue(json, INode.class);

        assertThat(tryNode, is(obj));
    }
}
