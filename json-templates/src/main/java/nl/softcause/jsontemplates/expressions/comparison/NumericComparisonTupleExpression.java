package nl.softcause.jsontemplates.expressions.comparison;

import static nl.softcause.jsontemplates.types.Types.BOOLEAN;
import static nl.softcause.jsontemplates.types.Types.DECIMAL;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

@EqualsAndHashCode(callSuper = false)
public abstract class NumericComparisonTupleExpression
        extends TupleExpression<java.lang.Boolean, java.lang.Double, java.lang.Double> {

    @Getter
    private IExpressionType returnType = BOOLEAN;

    NumericComparisonTupleExpression() {
        super(DECIMAL, DECIMAL, new ArrayList<>());
    }

    protected abstract java.lang.Boolean innerEvaluate(java.lang.Double lhs, java.lang.Double rhs);

    protected abstract String operator();

}
