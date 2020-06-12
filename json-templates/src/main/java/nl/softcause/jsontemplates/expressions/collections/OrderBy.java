package nl.softcause.jsontemplates.expressions.collections;

import static nl.softcause.jsontemplates.types.Types.LIST_OBJECT;
import static nl.softcause.jsontemplates.types.Types.TEXT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.util.TupleExpression;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;
import org.apache.commons.beanutils.BeanUtils;

@EqualsAndHashCode(callSuper = false)
public class OrderBy extends TupleExpression<List<Object>, List<Object>, String> {

    @Getter
    private IExpressionType returnType = LIST_OBJECT;

    public OrderBy() {
        super(LIST_OBJECT, TEXT, new ArrayList<>());
    }

    protected List<Object> innerEvaluate(List<Object> lhs, String on) {
        if (lhs == null) {
            return null;
        }
        var r = new ArrayList<>();
        r.addAll(lhs);
        Collections.sort(r, new PropertyComparator(on));
        return r;
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.FUNCTION;
    }


    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }


    private class PropertyComparator implements Comparator {

        private final String property;

        PropertyComparator(String property) {
            this.property = property;
        }

        @Override
        public int compare(Object o1, Object o2) {
            var v1 = getProperty(o1);
            var v2 = getProperty(o2);
            return new Order.DefaultComparator().compare(v1, v2);

        }

        private Object getProperty(Object subject) {
            Object value;
            try {
                value = subject != null ? BeanUtils.getSimpleProperty(subject, property) : null;
            } catch (Exception e) {
                throw TypeException.propertyAccess(property, subject.getClass());
            }
            return value;
        }
    }
}
