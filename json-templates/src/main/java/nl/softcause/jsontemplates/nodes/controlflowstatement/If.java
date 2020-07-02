package nl.softcause.jsontemplates.nodes.controlflowstatement;

import java.util.Map;

import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;

@EqualsAndHashCode(callSuper = true)
public class If extends ReflectionBasedNodeImpl {

    public static If create(Map<String, IExpression> arguments, Map<String, INode[]> slots) {
        var ifNode = new If();
        ifNode.setArguments(arguments);
        ifNode.setSlots(slots);
        return ifNode;
    }

    @RequiredArgument
    private boolean test;
    @RequiredSlot
    private INode thenNode;
    private INode elseNode;

    @Override
    protected void internalEvaluate(TemplateModel model) {
        if (test) {
            thenNode.evaluate(model);
        } else if (slots.containsKey("else")) {
            elseNode.evaluate(model);
        }
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase().add("If").expression(getArguments().get("test")).add("then").end();
        builder.describe(getSlots().get("then"));
        builder.describeIfPresent("else", getSlots().get("else"));
    }
}
