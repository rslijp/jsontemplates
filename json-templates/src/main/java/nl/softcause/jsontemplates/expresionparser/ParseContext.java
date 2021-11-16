package nl.softcause.jsontemplates.expresionparser;

import static nl.softcause.jsontemplates.expresionparser.ParseUtils.createExpression;
import static nl.softcause.jsontemplates.expresionparser.ParseUtils.operator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Pattern;

import lombok.NonNull;
import nl.softcause.jsontemplates.expressions.*;
import nl.softcause.jsontemplates.expressions.logic.Ternary;
import nl.softcause.jsontemplates.types.IExpressionType;

class ParseContext {

    private static final boolean LOG = false;
    private static final Pattern LONG_PATTERN = Pattern.compile("^(-?[0-9]+)");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^(-?[0-9]+\\.[0-9]+)");
    //    private static final Pattern TEXT_PATTERN = Pattern.compile("^'((\\w|\\s|[-!?.\";:{}/\\\\#])+)'");
    private static final Pattern TEXT_PATTERN = Pattern.compile("^'((?:[^'\\\\]|\\\\.)*)'");
    //    /'(?:[^'\\]|\\.)*'/
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("^\\$([0-9A-Za-z\\.]+)");
    private static final String BRACKET_OPEN = "(";
    private static final String PARAMETER_SEPARATOR = ",";
    private static final String BRACKET_CLOSE = ")";
    private static final Pattern NULL_TRAIL = Pattern.compile("^null([0-9A-Za-z\\.]+)");
    private static final Pattern TRUE_TRAIL = Pattern.compile("^true([0-9A-Za-z\\.]+)");
    private static final Pattern FALSE_TRAIL = Pattern.compile("^false([0-9A-Za-z\\.]+)");

    private Stack<IExpression> parseStack = new Stack<>();
    private final Stack<Stack<IExpression>> parseStackStash = new Stack<>();

    private final ParseCursor cursor;
    private Map<String, Class> unaryLib;
    private Map<String, Class> functionLib;
    private Map<String, Class> infixLib;

    ParseContext(@NonNull String text) {
        cursor = new ParseCursor(text);
    }

    ParseContext withFunctionLib(Map<String, Class> lib) {
        this.functionLib = lib;
        return this;
    }

    ParseContext withInfixLib(Map<String, Class> lib) {
        this.infixLib = lib;
        return this;
    }

    public ParseContext withUnaryLib(Map<String, Class> lib) {
        this.unaryLib = lib;
        return this;
    }

    void parseExpression(String[] until) {
        while (!done() && (until == null || !cursor.at(until))) {
            log("========");
            if (tryNullConstant() ||
                    tryLongConstant() ||
                    tryBooleanConstant() ||
                    tryDoubleConstant() ||
                    tryStringConstant() ||
                    tryVariable() ||
                    tryBrackets() ||
                    tryTernary() ||
                    libScan(unaryLib, this::tryUnary) ||
                    libScan(functionLib, this::tryFunction) ||
                    libScan(infixLib, this::tryInfix)) {
                continue;
            }
            throw ParseException.cantMatchHead().at(cursor);
        }
        reduce(until == null ? -1 : 0);
    }

    private boolean libScan(Map<String, Class> lib, Function<String, Boolean> tryParse) {
        var operators = lib
                .keySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> -e.length()))
                .toArray(String[]::new);

        for (var e : operators) {
            if (tryParse.apply(e)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryVariable() {
        if (cursor.at(VARIABLE_PATTERN)) {
            var txt = cursor.read(VARIABLE_PATTERN);
            push(new Variable(txt));
            return true;
        }
        return false;
    }

    private boolean tryNullConstant() {
        if (cursor.at("null") && !cursor.at(NULL_TRAIL)) {
            cursor.read("null");
            push(new Constant(null));
            return true;
        }
        return false;
    }

    private boolean tryBooleanConstant() {
        if (cursor.at("true") && !cursor.at(TRUE_TRAIL)) {
            cursor.read("true");
            push(new Constant(true));
            return true;
        }
        if (cursor.at("false") && !cursor.at(FALSE_TRAIL)) {
            cursor.read("false");
            push(new Constant(false));
            return true;
        }
        return false;
    }

    private boolean tryLongConstant() {
        if (!justSawConstant() &&
                !cursor.at(DOUBLE_PATTERN) &&
                cursor.at(LONG_PATTERN)) {
            var txt = cursor.read(LONG_PATTERN);
            push(new Constant(Long.parseLong(txt)));
            return true;
        }
        return false;
    }

    private boolean justSawConstant() {
        if (parseStack.empty()) {
            return false;
        }
        return parseStack.peek() instanceof Constant;
    }


    private boolean tryDoubleConstant() {
        if (!justSawConstant() &&
                cursor.at(DOUBLE_PATTERN)) {
            var txt = cursor.read(DOUBLE_PATTERN);
            push(new Constant(Double.parseDouble(txt)));
            return true;
        }
        return false;
    }


    private boolean tryStringConstant() {
        if (cursor.at(TEXT_PATTERN)) {
            var txt = cursor.read(TEXT_PATTERN);
            push(new Constant(txt));
            return true;
        }
        return false;
    }


    private boolean tryFunction(String functionName) {
        if (cursor.at(functionName)) {
            cursor.read(functionName);
            cursor.read(BRACKET_OPEN);
            var expr = (IExpressionWithArguments) createExpression(functionLib.get(functionName));
            IExpressionType[] argumentsTypes = expr.getArgumentsTypes();
            stashStack();
            for (var i = 0; i < argumentsTypes.length; i++) {
                var separators = new String[] {BRACKET_CLOSE, PARAMETER_SEPARATOR};
                parseExpression(separators);
                expr.getArguments().add(yield(expr.priority()));
                if (cursor.at(BRACKET_CLOSE)) {
                    ParseUtils.validateCompletenessOfArguments(expr, cursor);
                    cursor.read(BRACKET_CLOSE);
                    break;
                }
                cursor.read(PARAMETER_SEPARATOR);
            }
            popStack();
            push(expr);
            return true;
        }
        return false;
    }

    private void popStack() {
        if (!parseStack.empty()) {
            throw new RuntimeException("Bug");
        }
        parseStack = parseStackStash.pop();
    }

    private void stashStack() {
        parseStackStash.push(parseStack);
        parseStack = new Stack<>();

    }
//
//    private static void validateCompletenessOfArgumetns(IExpressionWithArguments expr, IExpressionType[] argumentsTypes, ParseCursor cursor) {
//        for (var j = expr.getArguments().size(); j < argumentsTypes.length; j++){
//            if(!(argumentsTypes[j] instanceof nl.softcause.jsontemplates.types.Optional)){
//                throw ParseException.expectedMoreArguments().at(cursor);
//            }
//        }
//    }

    private boolean tryInfix(String operator) {
        if (cursor.at(operator) && !empty()) {
            cursor.read(operator);
            var expr = (IExpressionWithArguments) createExpression(infixLib.get(operator));
            reduce(expr.priority());
            var lhs = parseStack.pop();
            log("POP " + operator(lhs));
            expr.getArguments().add(lhs);
            push(expr);
            return true;
        }
        return false;
    }

    private boolean tryUnary(String operator) {
        if (cursor.at(operator)) {
            cursor.read(operator);
            var expr = (IExpressionWithArguments) createExpression(unaryLib.get(operator));
            push(expr);
            return true;
        }
        return false;
    }

    private boolean tryBrackets() {
        if (cursor.at(BRACKET_OPEN)) {
            cursor.read(BRACKET_OPEN);
            parseExpression(new String[] {BRACKET_CLOSE});
            var inner = yield();
            var brackets = new Brackets();
            brackets.setArguments(Collections.singletonList(
                    inner
            ));
            push(brackets);
            cursor.read(BRACKET_CLOSE);
            return true;
        }
        return false;
    }


    private boolean tryTernary() {
        if (cursor.at("?") && !empty()) {
            cursor.read("?");
            var ternary = new Ternary();
            var condition = yield(ternary.priority());
            ternary.getArguments().add(condition);
            push(ternary);
            parseExpression(new String[] {":"});
            cursor.read(":");
            return true;
        }
        return false;
    }

    IExpression yield() {
        reduce(0);
        return parseStack.pop();
    }

    IExpression yield(int targetPriority) {
        reduce(targetPriority);
        return parseStack.pop();
    }

    private boolean done() {
        return !cursor.more();
    }

    boolean empty() {
        return parseStack.isEmpty();
    }

    private void push(IExpression expression) {
        log("PUSH " + operator(expression) + " " + cursor);
        parseStack.push(expression);
    }

    private void reduce(int targetPrio) {
        if (parseStack.size() >= 2) {
            IExpression argument = parseStack.pop();
            IExpression operator = parseStack.peek();
            var argumentPriority = argument.priority();
            var operatorPriority = operator.priority();
            var reduce = operatorPriority >= targetPrio && operator instanceof IExpressionWithArguments;

            if (argument instanceof Ternary &&
                    operator instanceof Ternary &&
                    targetPrio == 0
            ) {
                reduce = false;
            }

            if (LOG) {
                System.out.println(">>>>>>>>>");
                System.out.println("NEW: prio " + targetPrio + "");
                System.out.println("ARG:" + operator(argument) + "(prio " + argumentPriority + ")");
                System.out.println("OPR:" + operator(operator) + "(prio " + operatorPriority + ")");
                if (reduce) {
                    System.out.println("REDUCE");
                }
                System.out.println("<<<<<<<<");

            }
            if (reduce) {
                ((IExpressionWithArguments) operator).getArguments().add(argument);
                reduce(targetPrio);
            } else {
                parseStack.push(argument);
                //consume argument
            }
        }
    }

    private void log(String msg) {
        if (LOG) {
            System.out.println(msg);
        }
    }

    protected ParseCursor getCursor() {
        return cursor.clone();
    }

}
