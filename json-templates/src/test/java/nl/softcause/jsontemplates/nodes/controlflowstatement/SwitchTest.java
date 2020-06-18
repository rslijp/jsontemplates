package nl.softcause.jsontemplates.nodes.controlflowstatement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.AssertionNode;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.LimitedSlot;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import org.junit.Test;

public class SwitchTest {

    @Test
    public void should_collect_correct_arguments() {
        var chooseNode = new Switch();

        var argumentTypes = chooseNode.getArgumentsTypes();

        assertThat(argumentTypes, is(Collections.emptyMap()));
    }

    @Test
    public void should_collect_correct_slots() {
        var chooseNode = new Switch();

        var argumentTypes = chooseNode.getSlotTypes();

        assertThat(argumentTypes, is(Map.of(
                "caseNode", new LimitedSlot(new Class[] {Switch.Case.class}),
                "defaultNode", new OptionalSlot(new WildCardSlot())
        )));
    }

    @Test
    public void should_execute_first_case_when_test_is_true() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {assertionNodeSecond})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );

        switchNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNodeFirst.isCalled(), is(true));
        assertThat(assertionNodeSecond.isCalled(), is(false));
        assertThat(assertionNodeOther.isCalled(), is(false));
    }

    @Test
    public void should_execute_second_case_when_test_is_true() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeSecond})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );

        switchNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNodeFirst.isCalled(), is(false));
        assertThat(assertionNodeSecond.isCalled(), is(true));
        assertThat(assertionNodeOther.isCalled(), is(false));
    }

    @Test
    public void should_execute_default_node_when_no_test_is_true() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {assertionNodeSecond})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );

        switchNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNodeFirst.isCalled(), is(false));
        assertThat(assertionNodeSecond.isCalled(), is(false));
        assertThat(assertionNodeOther.isCalled(), is(true));
    }

    @Test
    public void should_onlt_execute_first_case_when_all_test_are_true() {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeSecond})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );

        switchNode.evaluate(new TemplateModel<>(new TestDefinition()));

        assertThat(assertionNodeFirst.isCalled(), is(true));
        assertThat(assertionNodeSecond.isCalled(), is(false));
        assertThat(assertionNodeOther.isCalled(), is(false));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var assertionNodeFirst = new AssertionNode();
        var assertionNodeSecond = new AssertionNode();
        var assertionNodeOther = new AssertionNode();
        var caseNodeFirst = Switch.Case.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("body", new INode[] {assertionNodeFirst})
        );
        var caseNodeSecond = Switch.Case.create(
                Collections.singletonMap("test", new Constant(false)),
                Map.of("body", new INode[] {assertionNodeSecond})
        );
        var switchNode = Switch.create(
                Map.of(
                        "case", new INode[] {caseNodeFirst, caseNodeSecond},
                        "default", new INode[] {assertionNodeOther}
                )
        );

        var json = new ObjectMapper().writeValueAsString(switchNode);

        var obj = new ObjectMapper().readValue(json, INode.class);

        assertThat(switchNode, is(obj));
    }
}
