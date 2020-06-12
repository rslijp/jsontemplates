package nl.softcause.jsontemplates.expressions.logic;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.OperatorPrecendence;

@EqualsAndHashCode(callSuper = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Or extends BooleanTupleExpression {

    @Override
    protected Boolean innerEvaluate(Boolean lhs, Boolean rhs) {
        return lhs || rhs;
    }

    @Override
    public String operator() {
        return "||";
    }


    @Override
    public Integer priority() {
        return OperatorPrecendence.OR;
    }
}
