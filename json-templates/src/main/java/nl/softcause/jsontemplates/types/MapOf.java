package nl.softcause.jsontemplates.types;

import lombok.Getter;
import lombok.Value;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Value
public class MapOf<T> implements IExpressionType<Map<String,T>> {

    private IExpressionType<T> baseType;

    @Override
    public String getType() {
        return baseType.getType().concat("[]");
    }

    @Override
    public boolean isA(Object src) {
        if(src==null) return true;
        if(src instanceof TypedHashMap){
            return baseType.isClassOfA(((TypedHashMap) src).getType());
        }
        return isClassOfA(src.getClass());
    }

    @Override
    public boolean isClassOfA(Class<?> src) {
        var elementType = elementClass(src);
        if(elementType==null) return false;
        return baseType.isClassOfA(elementType);
    }

    private Class elementOf(Object src) {
        if(src instanceof TypedHashMap){
            return ((TypedHashMap) src).getType();
        }
        return elementClass(src.getClass());
    }

    private Class elementClass(Class<?> src) {
        if(Map.class.isAssignableFrom(src)){
            try {
                Type sooper = src.getGenericInterfaces()[0];
                Type keyType = ((ParameterizedType) sooper).getActualTypeArguments()[0];
                if(!keyType.equals(String.class)){
                    throw TypeException.onlyMapWithStringKeysSupported(src);
                }
                return (Class) ((ParameterizedType) sooper).getActualTypeArguments()[1];
            } catch(Exception e) {
                throw TypeException.firstClassMapOnly(src);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String,T> convert(Object src) {
        if(!isA(src)) throw TypeException.invalidCast(src, this);
        if(src==null) return null;
        var yield = new TypedHashMap<T>(elementOf(src));
        if(src instanceof Map) {
            var iter= (Map) src;
            iter.forEach((key, value)-> {
                yield.put((String) key,baseType.convert(value));
            });
        }
        return yield;
    }

    @Override
    public IExpressionType baseType() { return baseType==Types.OBJECT ? baseType : Types.byName(Optional.name(baseType)); }

    @Override
    public String toString() {
        return getType();
    }

    private static class TypedHashMap<E> extends HashMap<String,E> {
        @Getter
        private final Class<E> type;

        private  TypedHashMap(Class<E> type){
            this.type=type;
        }
    }
}
