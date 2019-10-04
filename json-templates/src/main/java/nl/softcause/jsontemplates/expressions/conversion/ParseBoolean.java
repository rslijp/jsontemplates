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
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class ParseBoolean implements IExpressionWithArguments {
    @Getter
    @JsonInclude
    private List<IExpression> arguments;

    public ParseBoolean() {
        this(new ArrayList<>());
    }
    public ParseBoolean(List<IExpression> arguments) {
        this.arguments=arguments;
    }


    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return Types.OPTIONAL_BOOLEAN;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = (String) getArguments().get(0).evaluate(model);
        if(value!=null){
            var s = value.toLowerCase();
            if(s.equals("true") ||
                    value.toLowerCase().equals("y")){
                return true;
            }
            if(value.toLowerCase().equals("false") ||
                    value.toLowerCase().equals("n")){
                return false;
            }
            throw TypeException.conversionError(value, getReturnType(model));
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
