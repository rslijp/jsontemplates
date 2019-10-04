package nl.softcause.jsontemplates.expressions.text;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@EqualsAndHashCode(callSuper = true)
public class Concat extends TupleExpression<String, String, String>  {

    @Getter
    private IExpressionType returnType = TEXT;

    public Concat() {
        super(OPTIONAL_TEXT, OPTIONAL_TEXT, new ArrayList<>());
    }

    protected  String innerEvaluate(String lhs, String rhs){
        return (lhs==null?"":lhs).concat((rhs==null?"":rhs));
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
