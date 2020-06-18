package nl.softcause.jsontemplates.expressions.logic;

import nl.softcause.jsontemplates.OperatorPrecendence;

public class And extends BooleanTupleExpression {


    @Override
    protected Boolean innerEvaluate(Boolean lhs, Boolean rhs) {
        return lhs && rhs;
    }

    @Override
    public String operator() {
        return "&&";
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.AND;
    }
}
