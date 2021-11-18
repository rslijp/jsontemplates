package nl.softcause.jsontemplates.expressions.logic;

import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.model.IModel;

public class And extends BooleanTupleExpression {

    @Override
    protected Boolean innerEvaluate(IModel model) {
        return getLhs(model) && getRhs(model);
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
