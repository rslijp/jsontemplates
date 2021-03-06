package nl.softcause.jsontemplates.expressions.logic;

import static nl.softcause.jsontemplates.types.Types.BOOLEAN;

import java.util.ArrayList;
import java.util.List;

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

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Not implements IExpressionWithArguments {

    @JsonInclude
    @Getter
    private List<IExpression> arguments;

    @Getter
    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[] {BOOLEAN};

    public Not() {
        this(new ArrayList<>());
    }

    public Not(List<IExpression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return BOOLEAN;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = BOOLEAN.convert(getArguments().get(0).evaluate(model));
        return !value;
    }


    @Override
    public Integer priority() {
        return OperatorPrecendence.NOT;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.UNARY;
    }

    public String operator() {
        return "!";
    }

}

