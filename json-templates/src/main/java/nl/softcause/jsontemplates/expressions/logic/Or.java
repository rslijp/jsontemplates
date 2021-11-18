package nl.softcause.jsontemplates.expressions.logic;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.model.IModel;

@EqualsAndHashCode(callSuper = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Or extends BooleanTupleExpression {

    @Override
    protected Boolean innerEvaluate(IModel model) {
        return getLhs(model) || getRhs(model);
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
