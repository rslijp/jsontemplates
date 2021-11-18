package nl.softcause.jsontemplates.expressions.util;

import nl.softcause.jsontemplates.expressions.IExpression;

public interface ITupleExpression extends IExpression {
    IExpression getLhsArgument();
    IExpression getRhsArgument();

    Integer priority();
}
