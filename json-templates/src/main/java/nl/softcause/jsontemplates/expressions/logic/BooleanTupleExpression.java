package nl.softcause.jsontemplates.expressions.logic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;

import java.util.ArrayList;

import static nl.softcause.jsontemplates.types.Types.BOOLEAN;

@EqualsAndHashCode(callSuper = false)
public abstract class BooleanTupleExpression extends TupleExpression<java.lang.Boolean,java.lang.Boolean,java.lang.Boolean> {

    @Getter
    private IExpressionType returnType = BOOLEAN;

    BooleanTupleExpression() {
        super(BOOLEAN, BOOLEAN, new ArrayList<>());
    }

    protected abstract java.lang.Boolean innerEvaluate(java.lang.Boolean lhs, java.lang.Boolean rhs);

    public abstract String operator();

}
