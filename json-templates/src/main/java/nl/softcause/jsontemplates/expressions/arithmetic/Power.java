package nl.softcause.jsontemplates.expressions.arithmetic;

import static nl.softcause.jsontemplates.types.Types.DECIMAL;

import java.util.ArrayList;

import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.DownCastIfPossibleAnnotation;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

@DownCastIfPossibleAnnotation
public class Power extends TupleExpression<Double, Double, Double> {

    Power() {
        super(DECIMAL, DECIMAL, new ArrayList<>());
    }

    @Override
    protected Double innerEvaluate(Double lhs, Double rhs) {
        return Math.pow(lhs, rhs);
    }

    @Getter
    private IExpressionType returnType = DECIMAL;


    @Override
    public Integer priority() {
        return OperatorPrecendence.POWER;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.INFIX;
    }

    public String operator() {
        return "pow";
    }

}
