package nl.softcause.jsontemplates.expresionparser;

import static nl.softcause.jsontemplates.expresionparser.ParseUtils.createExpression;
import static nl.softcause.jsontemplates.expresionparser.ParseUtils.operator;

import java.util.HashMap;
import java.util.Map;

import nl.softcause.jsontemplates.expressions.Brackets;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.Variable;
import nl.softcause.jsontemplates.expressions.arithmetic.Abs;
import nl.softcause.jsontemplates.expressions.arithmetic.Add;
import nl.softcause.jsontemplates.expressions.arithmetic.Divide;
import nl.softcause.jsontemplates.expressions.arithmetic.Minus;
import nl.softcause.jsontemplates.expressions.arithmetic.Modulo;
import nl.softcause.jsontemplates.expressions.arithmetic.Multiply;
import nl.softcause.jsontemplates.expressions.arithmetic.Power;
import nl.softcause.jsontemplates.expressions.arithmetic.Random;
import nl.softcause.jsontemplates.expressions.arithmetic.Round;
import nl.softcause.jsontemplates.expressions.collections.Append;
import nl.softcause.jsontemplates.expressions.collections.AsList;
import nl.softcause.jsontemplates.expressions.collections.HasMember;
import nl.softcause.jsontemplates.expressions.collections.Head;
import nl.softcause.jsontemplates.expressions.collections.IsEmpty;
import nl.softcause.jsontemplates.expressions.collections.Order;
import nl.softcause.jsontemplates.expressions.collections.OrderBy;
import nl.softcause.jsontemplates.expressions.collections.Size;
import nl.softcause.jsontemplates.expressions.collections.Tail;
import nl.softcause.jsontemplates.expressions.comparison.Equals;
import nl.softcause.jsontemplates.expressions.comparison.GreaterThan;
import nl.softcause.jsontemplates.expressions.comparison.GreaterThanOrEqual;
import nl.softcause.jsontemplates.expressions.comparison.LessThan;
import nl.softcause.jsontemplates.expressions.comparison.LessThanOrEqual;
import nl.softcause.jsontemplates.expressions.comparison.NotEquals;
import nl.softcause.jsontemplates.expressions.conversion.FormatBoolean;
import nl.softcause.jsontemplates.expressions.conversion.FormatDate;
import nl.softcause.jsontemplates.expressions.conversion.FormatDouble;
import nl.softcause.jsontemplates.expressions.conversion.FormatInteger;
import nl.softcause.jsontemplates.expressions.conversion.NullOrDefault;
import nl.softcause.jsontemplates.expressions.conversion.ParseBoolean;
import nl.softcause.jsontemplates.expressions.conversion.ParseDate;
import nl.softcause.jsontemplates.expressions.conversion.ParseDouble;
import nl.softcause.jsontemplates.expressions.conversion.ParseInteger;
import nl.softcause.jsontemplates.expressions.logic.And;
import nl.softcause.jsontemplates.expressions.logic.Not;
import nl.softcause.jsontemplates.expressions.logic.Or;
import nl.softcause.jsontemplates.expressions.logic.Ternary;
import nl.softcause.jsontemplates.expressions.text.Concat;
import nl.softcause.jsontemplates.expressions.text.Contains;
import nl.softcause.jsontemplates.expressions.text.EndsWith;
import nl.softcause.jsontemplates.expressions.text.StartsWith;
import nl.softcause.jsontemplates.expressions.text.ToLower;
import nl.softcause.jsontemplates.expressions.text.ToUpper;

public class ExpressionParser {

    public static final Class[] DEFAULT_EXPRESIONS = new Class[] {
            Abs.class,
            Add.class,
            And.class,
            Append.class,
            AsList.class,
            Brackets.class,
            Concat.class,
            Constant.class,
            Contains.class,
            Divide.class,
            EndsWith.class,
            Equals.class,
            FormatBoolean.class,
            FormatDate.class,
            FormatDouble.class,
            FormatInteger.class,
            GreaterThan.class,
            GreaterThanOrEqual.class,
            HasMember.class,
            Head.class,
            IsEmpty.class,
            LessThan.class,
            LessThanOrEqual.class,
            Minus.class,
            Modulo.class,
            Multiply.class,
            Not.class,
            NotEquals.class,
            NullOrDefault.class,
            Or.class,
            Order.class,
            OrderBy.class,
            ParseBoolean.class,
            ParseDate.class,
            ParseDouble.class,
            ParseInteger.class,
            Power.class,
            Random.class,
            Round.class,
            Size.class,
            StartsWith.class,
            Tail.class,
            Ternary.class,
            ToLower.class,
            ToUpper.class,
            Variable.class};

    private static Map<String, Class> FUNCTION_LOOKUP = new HashMap<>();
    private static Map<String, Class> INFIX_LOOKUP = new HashMap<>();
    private static Map<String, Class> UNARY_LOOKUP = new HashMap<>();

    static {
        classInit();
    }

    private static void classInit() {
        for (var entry : DEFAULT_EXPRESIONS) {
            addExpression(entry);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static void addExpression(Class entry) {
        if (entry == Constant.class) {
            return;
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
