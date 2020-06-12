package nl.softcause.jsontemplates.expressions.arithmetic;

import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.DownCastIfPossibleAnnotation;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.ArrayList;

import static nl.softcause.jsontemplates.types.Types.DECIMAL;

@DownCastIfPossibleAnnotation
public class Modulo extends TupleExpression<Double,Double, Double> {

    public Modulo() {
        super(DECIMAL, DECIMAL, new ArrayList<>());
    }

    @Override
    protected Double innerEvaluate(Double lhs, Double rhs) {
        return lhs-Math.floor(lhs / rhs)*rhs;
    }

    @Getter
    private IExpressionType returnType = DECIMAL;

    @Override
    public Integer priority() {
        return OperatorPrecendence.MODULO;
    }

    public ExpressionParseType parseType(){
        return ExpressionParseType.INFIX;
    }

    public String operator(){
        return "mod";
    }
}
