package nl.softcause.jsontemplates.nodes;

import java.util.function.Function;

import lombok.Value;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.types.IExpressionType;

//@AllArgsConstructor
@Value
public class ArgumentDefinition<T> {
    IExpressionType<T> type;
    T defaultValue;
//    String fromModel;
//
//    public ArgumentDefinition(IExpressionType<T> type,T defaultValue){
//        this(type, defaultValue, null);
//    }

    public Function<IModel, T> bind(IExpression expression) {
        return (m) -> {
            if (expression == null) {
                return defaultValue;
            }
            var value = type.convert(expression.evaluate(m));
            if (value == null) {
                return defaultValue;
            }
            return value;
        };
    }
}
