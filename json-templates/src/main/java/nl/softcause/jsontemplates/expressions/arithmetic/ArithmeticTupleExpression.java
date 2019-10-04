package nl.softcause.jsontemplates.expressions.arithmetic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;

import java.util.ArrayList;

import static nl.softcause.jsontemplates.types.Types.DECIMAL;

@EqualsAndHashCode(callSuper = true)
public abstract class ArithmeticTupleExpression extends TupleExpression<Double,Double, Double> {


    @Getter
    private IExpressionType returnType = DECIMAL;

    ArithmeticTupleExpression() {
        super(DECIMAL, DECIMAL, new ArrayList<>());
    }

    protected abstract Double innerEvaluate(Double lhs, Double rhs);

    public abstract String operator();


}
