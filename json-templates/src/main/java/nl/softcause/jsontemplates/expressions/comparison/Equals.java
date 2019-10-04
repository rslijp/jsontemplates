package nl.softcause.jsontemplates.expressions.comparison;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.Types;

import java.util.ArrayList;

import static nl.softcause.jsontemplates.types.Types.OBJECT;
import static nl.softcause.jsontemplates.types.Types.BOOLEAN;

@EqualsAndHashCode(callSuper = false)
public class Equals extends TupleExpression<java.lang.Boolean, Object, Object> {

    @Getter
    private IExpressionType returnType = BOOLEAN;

    public Equals() {
        super(OBJECT, OBJECT, new ArrayList<>());
    }

    @Override
    protected java.lang.Boolean innerEvaluate(Object lhs, Object rhs) {
        if(lhs!=null){
            var lhsType = Types.determine(lhs).baseType();
            var rhsType = Types.determine(rhs).baseType();
            if(lhsType==Types.INTEGER && rhsType==Types.DECIMAL){
                lhs = Types.DECIMAL.convert(lhs);
            }
            if(lhsType==Types.DECIMAL && rhsType==Types.INTEGER){
                rhs = Types.OPTIONAL_DECIMAL.convert(rhs);
            }
            return lhs.equals(rhs);
        }
        return rhs==null;
    }

    public String operator() {
        return "==";
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.Equality;
    }




}
