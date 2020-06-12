package nl.softcause.jsontemplates.expressions.collections;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;

import java.util.ArrayList;
import java.util.List;

import static nl.softcause.jsontemplates.types.Types.LIST_GENERIC;

@Value
@EqualsAndHashCode(callSuper = false)
public class Append extends TupleExpression<List<Object>, List<Object>, List<Object>> {

    private IExpressionType returnType = LIST_GENERIC;

    public Append() {
        super(LIST_GENERIC, LIST_GENERIC, new ArrayList<>());
    }

    protected List<Object> innerEvaluate(List<Object> lhs, List<Object> rhs) {
        var r = new ArrayList<>();
        if (lhs != null) {
            r.addAll(lhs);
        }
        if (rhs != null) {
            r.addAll(rhs);
        }
        return r;
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.FUNCTION;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }

}
