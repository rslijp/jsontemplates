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
import nl.softcause.jsontemplates.types.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class FormatDate implements IExpressionWithArguments {

    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public FormatDate() {
        this(new ArrayList<>());
    }
    public FormatDate(List<IExpression> arguments) {
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
            var patternValue = getArguments().size()>=2 ? getArgument(model, 1) : null;
            var timeZone = getArguments().size()==3 ? getArgument(model, 2) : null;
            var format = DateFormatterUtils.buildFormatter(patternValue, timeZone, model.getLocale());
            return format.format(Date.from(Types.DATETIME.convert(value)));
        }
        return null;
    }

    private String getArgument(IModel model, int i) {
        var raw = getArguments().get(i);
        if(raw==null) return null;
        return Types.OPTIONAL_TEXT.convert(raw.evaluate(model));
    }


    @Override
    public IExpressionType[] getArgumentsTypes() {
        return new IExpressionType[]{Types.OPTIONAL_DATETIME, Types.OPTIONAL_TEXT};
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
