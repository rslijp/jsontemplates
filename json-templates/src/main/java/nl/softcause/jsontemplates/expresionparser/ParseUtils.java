package nl.softcause.jsontemplates.expresionparser;

import nl.softcause.jsontemplates.expressions.IExpression;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ParseUtils {
    public static String operator(IExpression expression) {
        var operator = expression.getClass().getSimpleName();
        operator = operator.substring(0,1).toLowerCase()+operator.substring(1);
        try {
            var candidate = expression.getClass().getMethod("operator");
            operator = (String) candidate.invoke(expression);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //
        }
        return operator;
    }

    public static IExpression createExpression(Class entry)  {
        try {
            var constructor = entry.getDeclaredConstructor(List.class);
            constructor.setAccessible(true);
            return (IExpression) constructor.newInstance(new ArrayList<>());
        } catch (Exception e) {
            try {
                var constructor = entry.getDeclaredConstructor();
                constructor.setAccessible(true);
                return (IExpression) constructor.newInstance();
            } catch (Exception e2) {
                throw new RuntimeException("BROKEN");
            }
        }
    }
}
