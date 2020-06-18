package nl.softcause.jsontemplates.expressions.conversion;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Types;

@EqualsAndHashCode
public class NullOrDefault implements IExpressionWithArguments {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public NullOrDefault() {
        this(new ArrayList<>());
    }

    public NullOrDefault(List<IExpression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return Types.GENERIC;
    }

    @Override
    public Object evaluate(@NonNull IModel model) {
        var value = getArguments().get(0).evaluate(model);
        if (value == null) {
            return getArguments().get(1).evaluate(model);
        }
        return value;
    }

    @Override
    public IExpressionType[] getArgumentsTypes() {
        return new IExpressionType[] {Types.OPTIONAL_GENERIC, Types.GENERIC};
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
