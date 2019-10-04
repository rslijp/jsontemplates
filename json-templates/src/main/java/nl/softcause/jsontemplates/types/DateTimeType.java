package nl.softcause.jsontemplates.types;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DateTimeType implements IExpressionType<Instant> {

    DateTimeType(){}

    @Override
    public String getType() {
        return "datetime";
    }

    @Override
    public boolean isA(Object src) {
        if(src==null) return false;
        return isClassOfA(src.getClass());
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        return  Date.class.isAssignableFrom(src) ||
                Instant.class.isAssignableFrom(src) ||
                Calendar.class.isAssignableFrom(src);

    }

    @Override
    public Instant convert(Object src) {
        if(!isA(src)) throw TypeException.invalidCast(src, this);
        Instant val = null;
        if (src instanceof Instant) {
            val = ((Instant) src);
        }
        if (src instanceof Date) {
            val = ((Date) src).toInstant();
        }
        if (src instanceof Calendar) {
            val = ((Calendar) src).toInstant();
        }
        return val;
    }

    @Override
    public IExpressionType baseType() { return this; }

    @Override
    public String toString() {
        return getType();
    }

}
