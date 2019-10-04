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

import java.util.List;

import static nl.softcause.jsontemplates.types.Types.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class Head implements IExpressionWithArguments {
    @JsonInclude
    private List<IExpression> arguments;

    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{LIST_GENERIC};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return OPTIONAL_GENERIC;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = LIST_GENERIC.convert(getArguments().get(0).evaluate(model));
        if(value==null || value.isEmpty()) return null;
        return value.get(0);
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