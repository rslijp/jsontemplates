package nl.softcause.jsontemplates.expressions.conversion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.expressions.ReduceOptionalAnnotation;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@ReduceOptionalAnnotation
public class ParseDouble implements IExpressionWithArguments {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public ParseDouble() {
        this(new ArrayList<>());
    }
    public ParseDouble(List<IExpression> arguments) {
        this.arguments=arguments;
    }

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return Types.OPTIONAL_DECIMAL;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = getArguments().get(0).evaluate(model);
        if(value!=null){
            try {
                NumberFormat format = NumberFormat.getInstance(model.getLocale());
                Number number = format.parse(Types.TEXT.convert(value));
                return Types.OPTIONAL_DECIMAL.convert(number.doubleValue());
            } catch (ParseException Pe){
                throw TypeException.conversionError(value, getReturnType(model));
            }
        }
        return null;
    }

    @Override
    public IExpressionType[] getArgumentsTypes() {
        return new IExpressionType[]{Types.OPTIONAL_TEXT};
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
