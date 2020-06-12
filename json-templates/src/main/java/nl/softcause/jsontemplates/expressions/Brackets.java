package nl.softcause.jsontemplates.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.List;

import static nl.softcause.jsontemplates.types.Types.GENERIC;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Brackets implements IExpressionWithArguments {

    @JsonInclude
    @Getter
    @Setter
    private List<IExpression> arguments;

    @JsonIgnore
    @Getter
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{GENERIC};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return GENERIC;
    }

    @Override
    public Object evaluate(IModel model) {
        return getArguments().get(0).evaluate(model);
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.BRACKETS;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.BRACKETS;
    }

}