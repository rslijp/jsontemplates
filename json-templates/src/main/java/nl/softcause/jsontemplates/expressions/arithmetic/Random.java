package nl.softcause.jsontemplates.expressions.arithmetic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;

import java.util.List;

import static nl.softcause.jsontemplates.types.Types.DECIMAL;
import static nl.softcause.jsontemplates.types.Types.OPTIONAL_DECIMAL;

@Value
public class Random implements IExpressionWithArguments {

    @JsonInclude
    private List<IExpression> arguments;

    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{OPTIONAL_DECIMAL};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return DECIMAL;
    }

    @Override
    public Object evaluate(IModel model) {
        var factor = 1.0;
        if(getArguments().size()==1){
            factor = DECIMAL.convert(getArguments().get(0).evaluate(model));
        }
        return factor*Math.random();
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
