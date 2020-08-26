package nl.softcause.jsontemplates.nodes.controlflowstatement;

import java.util.Map;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.base.MultiNode;

public class TestBuilderTool {

    public static INode node(INode... children) {
        return new MultiNode(children);
    }

    public static INode set(String name, String expression) {
        var s = new Set();
        s.setArguments(Map.of(
                "path", new Constant(name),
                "value", new ExpressionParser().parse(expression)));
        return s;
    }

    @SuppressWarnings("SameParameterValue")
    public static INode ifThen(String expression, INode then) {
        var s = new If();
        s.setArguments(Map.of(
                "test",  new ExpressionParser().parse(expression)));
        s.setSlots(Map.of(
                "then", new INode[]{then})
        );
        return s;
    }

    public static INode switchCases(Switch.Case... cases) {
        return Switch.create(Map.of(
                "case", cases
        ));
    }

    @SuppressWarnings("SameParameterValue")
    public static Switch.Case inCase(String expression, INode then) {
        var s = new Switch.Case();
        s.setArguments(Map.of(
                "test",  new ExpressionParser().parse(expression)));
        s.setSlots(Map.of(
                "body", new INode[]{then})
        );
        return s;
    }
}
