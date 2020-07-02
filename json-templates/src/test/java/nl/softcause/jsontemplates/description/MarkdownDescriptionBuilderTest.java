package nl.softcause.jsontemplates.description;


import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.comparison.Equals;
import nl.softcause.jsontemplates.nodes.AssertionNode;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.controlflowstatement.If;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Set;
import nl.softcause.jsontemplates.nodes.controlflowstatement.While;
import org.junit.Test;

public class MarkdownDescriptionBuilderTest {


    @Test
    public void should_describe_if_node_without_else() {
        var assertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Collections.singletonMap("then", new INode[] {assertionNode})
        );

        var builder = new MarkdownDescriptionBuilder();
        builder.describe(ifNode);

        assertThat(read("should_describe_if_node_without_else"), is(builder.asMarkdown()));
    }


    @Test
    public void should_describe_if_node() {
        var assertionNode = new AssertionNode();
        var ifNode = If.create(
                Collections.singletonMap("test", new Constant(true)),
                Map.of("then", new INode[] {assertionNode},
                        "else", new INode[] {assertionNode})
        );

        var builder = new MarkdownDescriptionBuilder();
        builder.describe(ifNode);

        assertThat(read("should_describe_if_node"), is(builder.asMarkdown()));
    }


    @Test
    public void should_describe_if_node_multiple_child_nodes() {
        var assertionNode = new AssertionNode();
        var testExpression = new Equals();
        testExpression.setArguments(Arrays.asList(new Variable("scope.current"), new Constant(2)));
        var ifNode = If.create(
                Collections.singletonMap("test", testExpression),
                Map.of("then", new INode[] {assertionNode, assertionNode},
                        "else", new INode[] {assertionNode})
        );

        var builder = new MarkdownDescriptionBuilder();
        builder.describe(ifNode);

        assertThat(read("should_describe_if_node_multiple_child_nodes"), is(builder.asMarkdown()));
    }


    @Test
    public void should_describe_deep_nesting() {
        var assertionNode = new AssertionNode();
        var testExpression = new Equals();
        testExpression.setArguments(Arrays.asList(new Variable("scope.current"), new Constant(2)));
        var whileNode = While.create(
                Collections.singletonMap("test", testExpression),
                Map.of("body", new INode[] {assertionNode, assertionNode})
        );
        var setNode = Set.create(
                Map.of("path", new Constant("foo"),
                        "value", new Constant("bar"))
        );
        var ifNode = If.create(
                Collections.singletonMap("test", testExpression),
                Map.of("then", new INode[] {whileNode, setNode},
                        "else", new INode[] {assertionNode})
        );


        var builder = new MarkdownDescriptionBuilder();
        builder.describe(ifNode);

        assertThat(read("should_describe_deep_nesting"), is(builder.asMarkdown()));
    }

    @Test
    public void should_describe_deep_nesting_block_style() {
        var assertionNode = new AssertionNode();
        var testExpression = new Equals();
        testExpression.setArguments(Arrays.asList(new Variable("scope.current"), new Constant(2)));
        var whileNode = While.create(
                Collections.singletonMap("test", testExpression),
                Map.of("body", new INode[] {assertionNode, assertionNode})
        );
        var setNode = Set.create(
                Map.of("path", new Constant("foo"),
                        "value", new Constant("bar"))
        );
        var ifNode1 = If.create(
                Collections.singletonMap("test", testExpression),
                Map.of("then", new INode[] {whileNode, setNode},
                        "else", new INode[] {assertionNode})
        );
        var ifNode2 = If.create(
                Collections.singletonMap("test", testExpression),
                Map.of("then", new INode[] {whileNode, setNode},
                        "else", new INode[] {assertionNode})
        );


        var builder = new MarkdownDescriptionBuilder().switchToBlockStyle();
        builder.describe(ifNode1, ifNode2);
        assertThat(read("should_describe_deep_nesting_block_style"), is(builder.asMarkdown()));
    }

    public String read(String test) {
        return new Scanner(MarkdownDescriptionBuilderTest.class.getResourceAsStream(test + ".md"), "UTF-8")
                .useDelimiter("\\A").next();
    }
}
