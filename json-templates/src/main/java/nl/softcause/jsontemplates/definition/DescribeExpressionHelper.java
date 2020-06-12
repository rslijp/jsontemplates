package nl.softcause.jsontemplates.definition;

import static nl.softcause.jsontemplates.expresionparser.ParseUtils.createExpression;
import static nl.softcause.jsontemplates.expresionparser.ParseUtils.operator;

import nl.softcause.jsontemplates.expressions.*;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.utils.ClassUtil;

public class DescribeExpressionHelper {

    private static  final boolean LOG = false;
    private final Class[] mainExpresions;

    DescribeExpressionHelper(Class[] mainExpresions)  {
        this.mainExpresions = mainExpresions;
    }

    void describe(ITemplateModelDefinition definition, TemplateDescription description) {
        log("EXPRESSIONS");
        for (var entry : mainExpresions) {
            description.add(describeExpression(entry, definition, description));
        }
    }


    private ExpressionDescription describeExpression(Class entry, ITemplateModelDefinition definition, TemplateDescription template) {
        if(entry == Constant.class) {
            return ExpressionDescription.CONSTANT;
        }
        if(entry == Variable.class) {
            return ExpressionDescription.VARIABLE;
        }
        IExpression expr = createExpression(entry);
        var description = new ExpressionDescription(template.newExpressionId(), operator(expr), entry.getSimpleName(), expr.priority(), expr.getReturnType(definition), expr.parseType());
        if(ClassUtil.hasAnnotation(expr, ReduceOptionalAnnotation.class)) {
            description.markReduceOptional();
        }
        if(ClassUtil.hasAnnotation(expr, DownCastIfPossibleAnnotation.class)) {
            description.markDownCastIfPossible();
        }
        if(expr instanceof IExpressionWithArguments) {
            var expArg = (IExpressionWithArguments) expr;
            for (var argType : expArg.getArgumentsTypes()) {
                description.addArgument(argType);
            }
        }
        log(description.toString());
        return description;
    }

    private void log(String line) {
        if(LOG) {
            System.out.println(line);
        }
    }
}
