package nl.softcause.jsontemplates.expressions.text;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;

import java.util.ArrayList;

import static nl.softcause.jsontemplates.types.Types.OPTIONAL_TEXT;
import static nl.softcause.jsontemplates.types.Types.TEXT;

@EqualsAndHashCode(callSuper = true)
public class StartsWith extends TupleExpression<Boolean, String, String>  {

    @Getter
    private final IExpressionType returnType = TEXT;

    public StartsWith() {
        super(OPTIONAL_TEXT, TEXT, new ArrayList<>());
    }

    protected  Boolean innerEvaluate(String lhs, String rhs){
        if(lhs==null) return false;
        return lhs.startsWith(rhs);
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
