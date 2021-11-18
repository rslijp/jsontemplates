package nl.softcause.jsontemplates.expressions.logic;

import static nl.softcause.jsontemplates.types.Types.BOOLEAN;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.expressions.util.LazyTupleExpression;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.types.IExpressionType;

@EqualsAndHashCode(callSuper = false)
public abstract class BooleanTupleExpression
        extends LazyTupleExpression<Boolean, Boolean, Boolean> {

    @Getter
    private final IExpressionType returnType = BOOLEAN;

    BooleanTupleExpression() {
        super(BOOLEAN, BOOLEAN, new ArrayList<>());
    }

    protected abstract java.lang.Boolean innerEvaluate(IModel model);

    public abstract String operator();

}
