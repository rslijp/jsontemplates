package nl.softcause.jsontemplates.expressions.arithmetic;

import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.DownCastIfPossibleAnnotation;

@DownCastIfPossibleAnnotation
public class Multiply extends ArithmeticTupleExpression {

    @Override
    protected Double innerEvaluate(Double lhs, Double rhs) {
        return lhs * rhs;
    }

    @Override
    public String operator() {
        return "*";
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.MULTIPLY;
    }
}
