package nl.softcause.jsontemplates.types;

public class BooleanType implements IExpressionType<java.lang.Boolean> {

    BooleanType() {
    }

    @Override
    public String getType() {
        return "boolean";
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
        return src == Boolean.class || src == boolean.class;
    }

    @Override
    public java.lang.Boolean convert(Object src) {
        if (!isA(src)) {
            throw TypeException.invalidCast(src, this);
        }
        return (boolean) src;
    }

    @Override
    public IExpressionType baseType() {
        return this;
    }

    @Override
    public IExpressionType<Boolean> infuse(Class<?> src) {
        return this;
    }

    @Override
    public String toString() {
        return getType();
    }

}
