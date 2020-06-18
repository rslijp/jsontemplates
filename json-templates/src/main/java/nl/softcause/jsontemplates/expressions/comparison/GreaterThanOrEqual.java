package nl.softcause.jsontemplates.expressions.comparison;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.softcause.jsontemplates.OperatorPrecendence;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class GreaterThanOrEqual extends NumericComparisonTupleExpression {


    @Override
    protected Boolean innerEvaluate(Double lhs, Double rhs) {
        return lhs >= rhs;
    }

    @Override
    public String operator() {
        return ">=";
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.COMPARISON;
    }
}
