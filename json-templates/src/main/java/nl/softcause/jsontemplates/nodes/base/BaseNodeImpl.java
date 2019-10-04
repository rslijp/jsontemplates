package nl.softcause.jsontemplates.nodes.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;

import java.util.Map;
import java.util.function.Function;


@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public abstract class BaseNodeImpl implements INode {
    @Getter
    protected Map<String, IExpression> arguments;
    @Getter
    protected Map<String, INode[]> slots;

    @Getter
    @JsonIgnore
    private final Map<String, ArgumentDefinition> argumentsTypes;

    @Getter
    @JsonIgnore
    private final Map<String, ISlotPattern> slotTypes;

    protected <T> Function<TemplateModel,T> argumentFunction(String argumentName) {
        return (Function<TemplateModel,T>) argumentsTypes.get(argumentName).bind(arguments.get(argumentName));
    }

    protected <T> T argumentValue(String argumentName, TemplateModel model) {
        return (T) argumentFunction(argumentName).apply(model);
    }

    public void evaluate(TemplateModel model) {
        this.registerDefinitions(model);
        internalEvaluate(model);
        this.revokeDefinitions(model);
    }

    protected abstract void internalEvaluate(TemplateModel model);
}
