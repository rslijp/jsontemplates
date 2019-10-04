package nl.softcause.jsontemplates.expressions.collections;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;

import java.util.ArrayList;
import java.util.List;

import static nl.softcause.jsontemplates.types.Types.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class HasMember extends TupleExpression<Boolean, List<Object>, Object>  {

    private IExpressionType returnType = BOOLEAN;

    public HasMember() {
        super(LIST_GENERIC, OPTIONAL_GENERIC, new ArrayList<>());
    }

    protected  Boolean innerEvaluate(List<Object> lhs, Object rhs){
        if(lhs==null) return false;
        return lhs.contains(rhs);
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.Function;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }


}
