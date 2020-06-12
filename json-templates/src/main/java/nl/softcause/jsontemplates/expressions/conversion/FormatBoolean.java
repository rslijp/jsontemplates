package nl.softcause.jsontemplates.expressions.conversion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.expressions.ReduceOptionalAnnotation;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Types;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@ReduceOptionalAnnotation
public class FormatBoolean implements IExpressionWithArguments {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public FormatBoolean() {
        this(new ArrayList<>());
    }

    public FormatBoolean(List<IExpression> arguments) {
        this.arguments=arguments;
    }


    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return Types.OPTIONAL_TEXT;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = getArguments().get(0).evaluate(model);
        if(value!=null){
            return Types.OPTIONAL_BOOLEAN.convert(value)?"Y":"N";

        }
        return null;
    }

    @Override
    public IExpressionType[] getArgumentsTypes() {
        return new IExpressionType[]{Types.OPTIONAL_BOOLEAN};
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
