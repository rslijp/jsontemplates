package nl.softcause.jsontemplates.types;

public class NullType implements IExpressionType<Object> {

    @Override
    public String getType() {
        return "null";
    }

    @Override
    public boolean isA(Object src) {
        return src == null;
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        return false;
    }

    @Override
    public Object convert(Object src) {
        if (!isA(src)) {
            throw TypeException.invalidCast(src, this);
        }
        return src;
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
