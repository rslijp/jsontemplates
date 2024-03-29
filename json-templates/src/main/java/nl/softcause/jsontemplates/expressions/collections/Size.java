package nl.softcause.jsontemplates.expressions.collections;

import static nl.softcause.jsontemplates.types.Types.INTEGER;
import static nl.softcause.jsontemplates.types.Types.LIST_OBJECT;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;

@Value
public class Size implements IExpressionWithArguments {

    @JsonInclude
    private List<IExpression> arguments;

    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[] {LIST_OBJECT};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return INTEGER;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = LIST_OBJECT.convert(getArguments().get(0).evaluate(model));
        if (value == null) {
            return 0;
        }
        return value.size();
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.FUNCTION;
    }


    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }


}
