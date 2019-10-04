package nl.softcause.jsontemplates.expressions.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Types;

import java.util.List;

import static nl.softcause.jsontemplates.types.Types.LIST_OBJECT;

@Value
@EqualsAndHashCode(callSuper = false)
public class IsEmpty implements IExpressionWithArguments {
    @JsonInclude
    private List<IExpression> arguments;

    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{LIST_OBJECT};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return Types.BOOLEAN;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = LIST_OBJECT.convert(getArguments().get(0).evaluate(model));
        return value == null || value.isEmpty();
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.Function;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }


}