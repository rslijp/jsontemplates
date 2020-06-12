package nl.softcause.jsontemplates.types;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Value;

@Value
public class ListOf<T> implements IExpressionType<List<T>> {

    private IExpressionType<T> baseType;

    @Override
    public String getType() {
        return baseType.getType().concat("*");
    }

    @Override
    public boolean isA(Object src) {
        if (src == null) {
            return true;
        }
        if (src instanceof TypedArrayList) {
            return baseType.isClassOfA(((TypedArrayList) src).getType());
        }
        return isClassOfA(src.getClass());
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        var elementType = elementClass(src);
        if (elementType == null) {
            return false;
        }
        return baseType.isClassOfA(elementType);
    }

    private Class elementOf(Object src) {
        if (src instanceof TypedArrayList) {
            return ((TypedArrayList) src).getType();
        }
        return elementClass(src.getClass());
    }

    private Class elementClass(Class<?> src) {
        if (src.isArray()) {
            return src.getComponentType();
        }
        if (!Map.class.isAssignableFrom(src) &&
                Iterable.class.isAssignableFrom(src)) {
            try {
                Type sooper = src.getGenericInterfaces()[0];
                Type elementType = ((ParameterizedType) sooper).getActualTypeArguments()[0];
                return (Class) elementType;
            } catch (Exception e) {
                throw TypeException.firstClassCollectionOnly(src);
            }

        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> convert(Object src) {
        if (!isA(src)) {
            throw TypeException.invalidCast(src, this);
        }
        if (src == null) {
            return null;
        }
        var yield = new TypedArrayList<>(elementOf(src));
        if (src.getClass().isArray()) {
            int length = Array.getLength(src);
            for (int i = 0; i < length; i++) {
                var arrayElement = Array.get(src, i);
                addElement(yield, arrayElement);
            }
        }
        if (src instanceof Iterable) {
            var iter = (Iterable) src;
            for (var iterElement : iter) {
                addElement(yield, iterElement);
            }
        }
        return yield;
    }

    private void addElement(TypedArrayList yield, Object arrayElement) {
        if (baseType == Types.GENERIC) {
            yield.add(Types.determineConstant(arrayElement).convert(arrayElement));
        } else {
            var optionalType = Types.byName(Optional.name(baseType));
            yield.add(optionalType.convert(arrayElement));
        }
    }

    @Override
    public IExpressionType baseType() {
        return (baseType == Types.OBJECT || baseType == Types.GENERIC) ? baseType :
                Types.byName(Optional.name(baseType));
    }

    @Override
    public String toString() {
        return getType();
    }

    private static class TypedArrayList<E> extends ArrayList<E> {
        @Getter
        private final Class<E> type;

        private TypedArrayList(Class<E> type) {
            this.type = type;
        }
    }
}
