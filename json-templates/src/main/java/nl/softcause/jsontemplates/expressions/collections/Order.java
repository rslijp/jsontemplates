package nl.softcause.jsontemplates.expressions.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.TypeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static nl.softcause.jsontemplates.types.Types.LIST_OBJECT;

@Value
@EqualsAndHashCode(callSuper = false)
public class Order implements IExpressionWithArguments {

    @JsonInclude
    private List<IExpression> arguments;

    @JsonIgnore
    private final IExpressionType[] argumentsTypes = new IExpressionType[]{LIST_OBJECT};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return LIST_OBJECT;
    }

    @Override
    public Object evaluate(IModel model) {
        var value = LIST_OBJECT.convert(getArguments().get(0).evaluate(model));
        if(value==null) return null;
        var r = new ArrayList<>(value);
        Collections.sort(r, new DefaultComparator());
        return r;
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.Function;
    }


    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }


    static class DefaultComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            if(o1==null && o2!=null) return Integer.MAX_VALUE;
            if(o1!=null && o2==null) return Integer.MIN_VALUE;
            if(o1==null && o2==null) return 0;

            if(o1 instanceof Comparable) {
                return ((Comparable) o1).compareTo(o2);
            }
            throw TypeException.notComparable(o1.getClass());
        }
    }
}
