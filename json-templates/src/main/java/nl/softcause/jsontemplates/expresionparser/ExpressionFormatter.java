package nl.softcause.jsontemplates.expresionparser;

import nl.softcause.jsontemplates.expressions.*;
import nl.softcause.jsontemplates.expressions.logic.Ternary;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.WordUtils;

import java.lang.reflect.InvocationTargetException;

import static nl.softcause.jsontemplates.expresionparser.ParseUtils.operator;

public class ExpressionFormatter {

    public String format(IExpression expression) {
        var pattern = expression.parseType();
        switch (pattern) {
            case CONSTANT:
                return formatConstant((Constant) expression);
            case VARIABLE:
                return formatVariable((Variable) expression);
            case BRACKETS:
                return formatBrackets((Brackets) expression);
            case UNARY:
                return formatUnary((IExpressionWithArguments) expression);
            case INFIX:
                return formatInfix((TupleExpression) expression);
            case FUNCTION:
                return formatFunction(expression);
            case TERNARY:
                return formatTernary((Ternary) expression);
            default:
                throw new RuntimeException("Not implemented formatter for "+pattern);
        }
    }

    private String formatTernary(Ternary expression) {
        var arguments = expression.getArguments();
        return String.format("%s ? %s : %s", format(arguments.get(0)),format(arguments.get(1)),format(arguments.get(2)));
    }


    private String formatConstant(Constant expression) {
        return expression.toString();
    }

    private String formatBrackets(Brackets expression) {
        return String.format("(%s)", format(expression.getArguments().get(0)));
    }

    private String formatVariable(Variable expression) {
        return String.format("%s%s", operator(expression), expression.getName());
    }

    private String formatUnary(IExpressionWithArguments expression) {
        return String.format("%s%s", operator(expression), format(expression.getArguments().get(0)));
    }



    private String formatInfix(TupleExpression expression) {
        var lhs = (IExpression) expression.getArguments().get(0);
        var rhs =  (IExpression) expression.getArguments().get(1);
        var operator = operator(expression);
        var builder = new StringBuilder();
        if(lhs.priority()!=null && lhs.priority() <= expression.priority()){
            builder.append(String.format("(%s)", format(lhs)));
        } else {
            builder.append( format(lhs));
        }
        builder.append(" ");
        builder.append(operator);
        builder.append(" ");
        if(rhs.priority()!=null && rhs.priority() <= expression.priority()){
            builder.append(String.format("(%s)", format(rhs)));
        } else {
            builder.append( format(rhs));
        }
        return builder.toString();
    }

    private String formatFunction(IExpression expression) {
        var builder = new StringBuilder();
        builder.append(operator(expression));
        builder.append("(");
        if(expression instanceof  IExpressionWithArguments) {
            var withArguments = (IExpressionWithArguments) expression;
            var first = true;
            for (var i = 0; i < withArguments.getArguments().size(); i++) {
                var argument = (IExpression) withArguments.getArguments().get(i);
                if (!first) builder.append(",");
                builder.append(format(argument));
                first = false;
            }
        }
        builder.append(")");
        return builder.toString();
    }

}
