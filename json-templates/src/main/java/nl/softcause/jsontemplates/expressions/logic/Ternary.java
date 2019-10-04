package nl.softcause.jsontemplates.expressions.logic;

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

import static nl.softcause.jsontemplates.types.Types.BOOLEAN;
import static nl.softcause.jsontemplates.types.Types.GENERIC;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Ternary implements IExpressionWithArguments {

    @JsonInclude
    @Getter
    private List<IExpression> arguments;

    @Getter
    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{BOOLEAN, GENERIC, GENERIC};

    public Ternary() {
        this(new ArrayList<>());
    }
    public Ternary(List<IExpression> arguments) {
        this.arguments=arguments;
    }

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return GENERIC;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = BOOLEAN.convert(getArguments().get(0).evaluate(model));
        return (value?getArguments().get(1):getArguments().get(2)).evaluate(model);
    }

    @Override
    public Integer priority() {
        return arguments!=null && arguments.size()>=2?OperatorPrecendence.Function:OperatorPrecendence.Ternary;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.TERNARY;
    }

}

