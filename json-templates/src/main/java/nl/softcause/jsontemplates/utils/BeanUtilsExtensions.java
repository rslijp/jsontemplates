package nl.softcause.jsontemplates.utils;

import nl.softcause.jsontemplates.types.TypeException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class BeanUtilsExtensions {

    public static Class resolveClass(Class src, boolean resolveElementType){
        if(!resolveElementType){
            return src;
        }
        if(src.isArray()) {
            return src.getComponentType();
        }
        if(Map.class.isAssignableFrom(src)) {
            try {
                Type sooper = src.getGenericInterfaces()[0];
                Type keyType = ((ParameterizedType) sooper).getActualTypeArguments()[0];
                if(!keyType.equals(String.class)){
                    throw TypeException.onlyMapWithStringKeysSupported(src);
                }
                Type elementType = ((ParameterizedType) sooper).getActualTypeArguments()[1];
                return (Class) elementType;
            } catch(Exception e) {
                throw TypeException.firstClassMapOnly(src);
            }
        }
        if(Iterable.class.isAssignableFrom(src)){
            try {
                Type sooper = src.getGenericInterfaces()[0];
                Type elementType = ((ParameterizedType) sooper).getActualTypeArguments()[0];
                return (Class) elementType;
            } catch(Exception e) {
                throw TypeException.firstClassCollectionOnly(src);
            }

        }
        return src;
    }
}
