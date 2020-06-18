package nl.softcause.jsontemplates.expressions.collections;

import static nl.softcause.jsontemplates.types.Types.*;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

@Value
@EqualsAndHashCode(callSuper = false)
public class HasMember extends TupleExpression<Boolean, List<Object>, Object> {

    private IExpressionType returnType = BOOLEAN;

    public HasMember() {
        super(LIST_GENERIC, OPTIONAL_GENERIC, new ArrayList<>());
    }

    protected Boolean innerEvaluate(List<Object> lhs, Object rhs) {
        if (lhs == null) {
            return false;
        }
        return lhs.contains(rhs);
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
