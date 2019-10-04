package nl.softcause.jsontemplates.types;

import lombok.Value;

@Value
public class Optional<T> implements IExpressionType<T> {

    private IExpressionType<T> baseType;

    public static <T> String name(IExpressionType<T> candidate) {
        if(candidate.getType().endsWith("?")) return candidate.getType();
        return candidate.getType().concat("?");
    }

    @Override
    public String getType() {
        return name(baseType);
    }

    @Override
    public boolean isA(Object src) {
        if(src==null) return true;
        return baseType.isA(src);
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        if(src.isPrimitive()) return false;
        return baseType.isClassOfA(src);
    }

    @Override
    public T convert(Object src) {
        if(src==null) return null;
        if(!baseType.isA(src)) throw TypeException.invalidCast(src, this);
        return baseType.convert(src);
    }

    @Override
    public IExpressionType baseType() { return baseType; }

    @Override
    public String toString() {
        return getType();
    }
}
