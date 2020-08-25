package nl.softcause.jsontemplates.nodes.controlflowstatement;

import java.util.Collections;
import java.util.Map;

import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.types.Types;

@EqualsAndHashCode(callSuper = true)
public class Set extends ReflectionBasedNodeImpl {

    public static Set create(Map<String, IExpression> arguments) {
        var node = new Set();
        node.setArguments(arguments);
        node.setSlots(Collections.emptyMap());
        return node;
    }

    @RequiredArgument
    private String path;


    //    @TypeFromModel(path="path")
    private Object value;

    @SuppressWarnings("unchecked")
    @Override
    public void registerDefinitions(ITemplateModelDefinition model) {
        var type = getArguments().get("value").getReturnType(model);
        var path = Types.TEXT.convert(getArguments().get("path").evaluate(null));
        getArgumentsTypes().put("value", new ArgumentDefinition(type, null));
        if (path.startsWith("scope.")) {
            model.addDefinition(path.substring("scope.".length()), type, null, true, true, null, null);
        }
    }

    @Override
    public void internalEvaluate(TemplateModel model) {
        model.set(path, value);
    }


    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase()
                .add("Set")
                .expression(getArguments().get("path"))
                .add("to")
                .expression(getArguments().get("value"))
                .end();
    }
}
