package nl.softcause.jsontemplates.types;

public class GenericType implements IExpressionType<Object> {

    GenericType() {
    }

    public static IExpressionType resolveT(IExpressionType genericType, IExpressionType concreteType) {
        return Types.byName(genericType.getType().replace("T", concreteType.baseType().baseType().getType()));
    }

    public static IExpressionType inferT(IExpressionType concreteType) {
        return Types.byName(concreteType.getType().replace(concreteType.baseType().getType(), "T"));
    }


    @Override
    public String getType() {
        return "T";
    }

    @Override
    public boolean isA(Object src) {
        return src != null;
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
    public String toString() {
        return getType();
    }

}
