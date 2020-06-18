package nl.softcause.jsontemplates.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DecimalType implements IExpressionType<Double> {

    DecimalType() {
    }

    @Override
    public String getType() {
        return "decimal";
    }

    @Override
    public boolean isA(Object src) {
        if (src == null) {
            return false;
        }
        return isClassOfA(src.getClass());
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        return (src != BigDecimal.class &&
                src != BigInteger.class) &&
                Number.class.isAssignableFrom(src) ||
                (src == double.class ||
                        src == float.class ||
                        src == int.class ||
                        src == long.class);
    }

    @Override
    public Double convert(Object src) {
        if (!isA(src)) {
            throw TypeException.invalidCast(src, this);
        }
        Double val = null;
        if (src instanceof Number) {
            val = ((Number) src).doubleValue();
        }
        return val;
    }

    @Override
    public IExpressionType baseType() {
        return this;
    }

    @Override
    public String toString() {
        return getType();
    }

}
