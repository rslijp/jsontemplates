package nl.softcause.jsontemplates.types;

public class IntegerType implements IExpressionType<Long> {

    IntegerType(){}

    @Override
    public String getType() {
        return "integer";
    }

    @Override
    public boolean isA(Object src) {
        if(src==null) return false;
        return isClassOfA(src.getClass());
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        return src == Integer.class || src == Long.class || src == int.class || src == long.class;
    }

    @Override
    public Long convert(Object src) {
        if(!isA(src)) throw TypeException.invalidCast(src, this);
        Long val = null;
        if (src instanceof Number) {
            val = ((Number) src).longValue();
        }
        return (long) val;
    }

    @Override
    public IExpressionType baseType() { return this; }

    @Override
    public String toString() {
        return getType();
    }

}
