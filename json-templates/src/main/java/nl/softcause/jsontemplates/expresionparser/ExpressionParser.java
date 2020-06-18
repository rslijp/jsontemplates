package nl.softcause.jsontemplates.expresionparser;

import static nl.softcause.jsontemplates.expresionparser.ParseUtils.createExpression;
import static nl.softcause.jsontemplates.expresionparser.ParseUtils.operator;

import java.util.HashMap;
import java.util.Map;

import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.utils.ClassUtil;

public class ExpressionParser {

    private static boolean initialized = false;
    private static Map<String, Class> FUNCTION_LOOKUP = new HashMap<>();
    private static Map<String, Class> INFIX_LOOKUP = new HashMap<>();
    private static Map<String, Class> UNARY_LOOKUP = new HashMap<>();

    public ExpressionParser() {
        if (!initialized) {
            classInit();
        }
    }


    synchronized private void classInit() {
        if (initialized) {
            return;
        }
        var listing = ClassUtil.listAllExpressions();
        for (var entry : listing) {
            if (entry == Constant.class) {
                continue;
            }
            IExpression expr = createExpression(entry);
            if (expr.parseType() == ExpressionParseType.UNARY) {
                UNARY_LOOKUP.put(operator(expr), entry);
            }
            if (expr.parseType() == ExpressionParseType.FUNCTION) {
                FUNCTION_LOOKUP.put(operator(expr), entry);
            }
            if (expr.parseType() == ExpressionParseType.INFIX) {
                INFIX_LOOKUP.put(operator(expr), entry);
            }
        }
        initialized = true;
    }


    public IExpression parse(String text) {
        var context = new ParseContext(text)
                .withUnaryLib(UNARY_LOOKUP)
                .withFunctionLib(FUNCTION_LOOKUP)
                .withInfixLib(INFIX_LOOKUP);
        context.parseExpression(null);
        var result = context.yield();
        if (context.empty()) {
            ParseUtils.validateCompletenessOfArguments(result, context.getCursor());
            return result;
        }
        throw ParseException.stackNotEmpty(text);
    }

}
