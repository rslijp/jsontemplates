package nl.softcause.jsontemplates.expressions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Optional;
import nl.softcause.jsontemplates.types.Types;

@Getter
@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Constant implements IExpression {

    private Object value;

    public Constant(){}

    public Constant(Object value) {
        this.value = value;
        var type = determineConstantType();
        if (type.baseType() == Types.TEXT) {
            this.value = value.toString().replaceAll("\\\\'", "'");
        }
    }

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return determineConstantType();
    }

    private IExpressionType determineConstantType() {
        var type = Types.determineConstant(value);
        if (type.baseType() == Types.OBJECT) {
            type = Types.decorate(type, value.getClass());
        }
        return type;
    }

    @Override
    public Object evaluate(IModel model) {
        return determineConstantType().convert(value);
    }

    @Override
    public Integer priority() {
        return null;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.CONSTANT;
    }


    public String toString() {
        var type = determineConstantType();
        if (type instanceof Optional) {
            if (value == null) {
                throw new RuntimeException("kak");
            }
            type = type.baseType();
        }
        if (type == Types.TEXT) {
            var escaped = value.toString().replaceAll("'", "\\\\'");
            return String.format("'%s'", escaped);
        }
        if (type == Types.INTEGER || type == Types.DECIMAL) {
            return String.valueOf(value);
        }
        if (type == Types.BOOLEAN) {
            return String.format((boolean) value ? "true" : "false");
        }
        if (type == Types.NULL) {
            return "null";
        }
//        if(type==Types.DATETIME){
//            return DateFormatterUtils.formatToIso((Instant) value);
//        }
        throw new RuntimeException("kak");
    }
}
