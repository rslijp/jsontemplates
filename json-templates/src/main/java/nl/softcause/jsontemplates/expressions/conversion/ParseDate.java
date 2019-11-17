package nl.softcause.jsontemplates.expressions.conversion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.expressions.util.DateFormatterUtils;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class ParseDate implements IExpressionWithArguments {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public ParseDate() {
        this(new ArrayList<>());
    }
    public ParseDate(List<IExpression> arguments) {
        this.arguments=arguments;
    }


    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return Types.OPTIONAL_DATETIME;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = getArguments().get(0).evaluate(model);
        if(value!=null){
            try {
                var patternValue = getArguments().size()>=2 ? Types.OPTIONAL_TEXT.convert(getArguments().get(1).evaluate(model)) : null;
                var timeZone = getArguments().size()==3 ? Types.OPTIONAL_TEXT.convert(getArguments().get(2).evaluate(model)) : null;
                var format = DateFormatterUtils.buildFormatter(patternValue, timeZone,model.getLocale());
                Date date = format.parse(Types.TEXT.convert(value));
                return Types.DATETIME.convert(date);
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
