package nl.softcause.jsontemplates.expressions.text;

import static nl.softcause.jsontemplates.types.Types.OPTIONAL_TEXT;
import static nl.softcause.jsontemplates.types.Types.TEXT;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@EqualsAndHashCode(callSuper = false)
public class EndsWith extends TupleExpression<Boolean, String, String> {

    @Getter
    private IExpressionType returnType = TEXT;

    public EndsWith() {
        super(OPTIONAL_TEXT, TEXT, new ArrayList<>());
    }

    protected Boolean innerEvaluate(String lhs, String rhs) {
        if (lhs == null) {
            return false;
        }
        return lhs.endsWith(rhs);
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
