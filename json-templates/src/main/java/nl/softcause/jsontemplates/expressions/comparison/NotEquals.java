package nl.softcause.jsontemplates.expressions.comparison;

import static nl.softcause.jsontemplates.types.Types.BOOLEAN;
import static nl.softcause.jsontemplates.types.Types.OBJECT;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

@EqualsAndHashCode(callSuper = false)
public class NotEquals extends TupleExpression<Boolean, Object, Object> {

    @Getter
    private IExpressionType returnType = BOOLEAN;

    public NotEquals() {
        super(OBJECT, OBJECT, new ArrayList<>());
    }

    @Override
    protected Boolean innerEvaluate(Object lhs, Object rhs) {
        if (lhs != null) {
            return !lhs.equals(rhs);
        }
        return rhs != null;
    }

    public String operator() {
        return "!=";
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.EQUALITY;
    }

}
