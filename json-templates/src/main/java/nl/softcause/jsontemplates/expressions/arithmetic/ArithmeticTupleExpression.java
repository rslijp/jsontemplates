package nl.softcause.jsontemplates.expressions.arithmetic;

import static nl.softcause.jsontemplates.types.Types.DECIMAL;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

@EqualsAndHashCode(callSuper = true)
public abstract class ArithmeticTupleExpression extends TupleExpression<Double, Double, Double> {


    @Getter
    private IExpressionType returnType = DECIMAL;

    ArithmeticTupleExpression() {
        super(DECIMAL, DECIMAL, new ArrayList<>());
    }

    protected abstract Double innerEvaluate(Double lhs, Double rhs);

    public abstract String operator();


}
