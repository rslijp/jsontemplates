package nl.softcause.jsontemplates.nodes.controlflowstatement;

import java.util.Map;

import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithDefaultScopeImpl;

@EqualsAndHashCode(callSuper = true)
public class While extends ReflectionBasedNodeWithDefaultScopeImpl {


    public static While create(Map<String, IExpression> arguments, Map<String, INode[]> slots) {
        var node = new While();
        node.setArguments(arguments);
        node.setSlots(slots);
        return node;
    }

    @RequiredArgument
    private boolean test = false;
    @RequiredSlot
    private INode bodyNode = null;


    @Override
    public void internalEvaluate(TemplateModel model) {
        while (test) {
            bodyNode.evaluate(model);
            populateField(model, "test");
        }
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase()
                .add("While").
                expression(getArguments().get("test")).
                add("holds do").
                end();
        builder.describe(getSlots().get("body"));
    }

}
