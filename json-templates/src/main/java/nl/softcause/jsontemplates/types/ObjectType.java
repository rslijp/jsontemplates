package nl.softcause.jsontemplates.types;

public class ObjectType implements IExpressionType<Object> {

    ObjectType() {
    }

    @Override
    public String getType() {
        return "object";
    }

    @Override
    public boolean isA(Object src) {
        return true;
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        return true;
    }

    @Override
    public Object convert(Object src) {
        return src;
    }

    @Override
    public IExpressionType baseType() {
        return this;
    }

    @Override
    public IExpressionType<Object> infuse(Class<?> src) {
        return this;
    }

    @Override
    public String toString() {
        return getType();
    }
}
