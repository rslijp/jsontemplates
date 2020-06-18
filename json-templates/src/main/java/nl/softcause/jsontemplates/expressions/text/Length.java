package nl.softcause.jsontemplates.expressions.text;

import static nl.softcause.jsontemplates.types.Types.INTEGER;
import static nl.softcause.jsontemplates.types.Types.OPTIONAL_TEXT;

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
public class Length implements IExpressionWithArguments {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public Length() {
        this(new ArrayList<>());
    }

    public Length(List<IExpression> arguments) {
        this.arguments = arguments;
    }

    @Getter
    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[] {OPTIONAL_TEXT};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return INTEGER;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = OPTIONAL_TEXT.convert(getArguments().get(0).evaluate(model));
        if (value == null) {
            return 0;
        }
        return value.length();
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
