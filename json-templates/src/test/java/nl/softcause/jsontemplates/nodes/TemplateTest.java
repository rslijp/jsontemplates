package nl.softcause.jsontemplates.nodes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.comparison.Equals;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.model.TestDefinition;
import nl.softcause.jsontemplates.nodes.base.MultiNode;
import nl.softcause.jsontemplates.nodes.controlflowstatement.For;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import org.junit.Test;

public class TemplateTest {

    @Test
    public void should_evaluate_simple_template() {
        var setNode = Set.create(
                Map.of("path", new Constant("age"),
                        "value", new Variable("scope.current"))
        );

        var testExpression = new Equals();
        testExpression.setArguments(Arrays.asList(new Variable("scope.current"), new Constant(2)));
        var ifNode = If.create(
                Collections.singletonMap("test", testExpression),
                Collections.singletonMap("then", new INode[] {setNode})
        );

        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {ifNode})
        );

        var model = new TestDefinition();

        forNode.evaluate(new TemplateModel<>(model));

        assertThat(model.getAge(), is(2));
//        assertThat(model.getName(), is("Hello world"));

    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var setNode = Set.create(
                Map.of("path", new Constant("age"),
                        "value", new Variable("scope.current"))
        );

        var testExpression = new Equals();
        testExpression.setArguments(Arrays.asList(new Variable("scope.current"), new Constant(2)));
        var ifNode = If.create(
                Collections.singletonMap("test", testExpression),
                Collections.singletonMap("then", new INode[] {setNode})
        );

        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {ifNode})
        );

        var json = new ObjectMapper().writeValueAsString(forNode);
        System.out.println(json);
        var obj = new ObjectMapper().readValue(json, INode.class);

        assertThat(forNode, is(obj));
    }


    @Test
    public void should_have_a_stack_depth_of_one_for_simple_template() {
        //Given
        var assertionNode = new AssertionNode();
        assertionNode.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(1));
            return null;
        });


        var model = new TestDefinition();

        assertionNode.evaluate(new TemplateModel<>(model));
    }

    @Test
    public void should_not_increase_stack_for_no_stack_pushing_node() {
        //Given
        var assertionNode = new AssertionNode();
        assertionNode.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(1));
            return null;
        });
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Collections.singletonMap("then", new INode[] {assertionNode})
        );

        var model = new TestDefinition();

        //When
        ifNode.evaluate(new TemplateModel<>(model));
    }

    @Test
    public void should_increase_stack_by_one_for_stack_pushing_node() {
        //Given
        var assertionNode = new AssertionNode();
        assertionNode.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(2));
            return null;
        });
        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );

        var model = new TestDefinition();

        //When
        forNode.evaluate(new TemplateModel<>(model));
    }

    @Test
    public void should_have_a_stack_depth_of_one_for_multi_node_template() {
        //Given
        var assertionNode = new AssertionNode();
        assertionNode.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(1));
            return null;
        });
        var mulitNode = new MultiNode(new INode[] {assertionNode, assertionNode});

        var model = new TestDefinition();

        //When
        mulitNode.evaluate(new TemplateModel<>(model));
    }

    @Test
    public void should_not_increase_stack_for_no_stack_pushing_node_in_multinode() {
        //Given
        var assertionNode = new AssertionNode();
        assertionNode.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(1));
            return null;
        });
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Collections.singletonMap("then", new INode[] {assertionNode})
        );

        var mulitNode = new MultiNode(new INode[] {ifNode, ifNode});

        var model = new TestDefinition();

        //When
        mulitNode.evaluate(new TemplateModel<>(model));
    }

    @Test
    public void should_have_a_stack_depth_of_one_for_multi_node_template_in_multinode() {
        //Given
        var assertionNode = new AssertionNode();
        assertionNode.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(2));
            return null;
        });
        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNode})
        );
        var mulitNode = new MultiNode(new INode[] {forNode, forNode});

        var model = new TestDefinition();

        //When
        mulitNode.evaluate(new TemplateModel<>(model));
    }

    @Test
    public void should_behave_nice_in_complex_stack() {
        //Given
        var assertionNodeTop = new AssertionNode().validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(1));
            assertThat(m.scope().hasDefinition("somevalue"), is(false));
            return null;
        });

        var assertionNodeBeforeFor = new AssertionNode();
        assertionNodeBeforeFor.validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(2));
            assertThat(m.scope().hasDefinition("somevalue"), is(assertionNodeBeforeFor.getCounter() > 1));
            return null;
        });
        var assertionNodeAfterFor = new AssertionNode().validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(2));
            assertThat(m.scope().hasDefinition("somevalue"), is(true));
            return null;
        });

        var assertionNodeNestedFor = new AssertionNode().validate(m -> {
            //Then
            assertThat(m.scopeDepth(), is(3));
            assertThat(m.scope().hasDefinition("somevalue"), is(false));
            return null;
        });
        var setNode = Set.create(
                Map.of("path", new Constant("scope.somevalue"),
                        "value", new Constant("42"))
        );
        var forNestedNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body", new INode[] {assertionNodeNestedFor})
        );
        var forNode = For.create(
                Collections.singletonMap("until", new Constant(3)),
                Collections.singletonMap("body",
                        new INode[] {assertionNodeBeforeFor, setNode, forNestedNode, assertionNodeAfterFor})
        );

        var mulitNode = new MultiNode(new INode[] {assertionNodeTop, forNode});

        var model = new TestDefinition();

        //When
        mulitNode.evaluate(new TemplateModel<>(model));
    }
}
