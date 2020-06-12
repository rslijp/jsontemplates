package nl.softcause.jsontemplates.expressions.arithmetic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.ArrayList;
import java.util.List;

import static nl.softcause.jsontemplates.types.Types.*;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Round implements IExpressionWithArguments, IExpression {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public Round() {
        this(new ArrayList<>());
    }

    public Round(List<IExpression> arguments) {
        this.arguments=arguments;
    }

    @Getter
    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{DECIMAL,OPTIONAL_INTEGER};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return DECIMAL;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = DECIMAL.convert(getArguments().get(0).evaluate(model));
        var factor = 0L;
        if(getArguments().size()==2){
            factor = INTEGER.convert(getArguments().get(1).evaluate(model));
        }
        var mupltiplier = Math.pow(10.0, factor);
        return Math.round(value*mupltiplier)/mupltiplier;
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
