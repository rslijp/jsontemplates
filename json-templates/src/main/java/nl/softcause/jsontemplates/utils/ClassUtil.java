package nl.softcause.jsontemplates.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassUtil {
    public static boolean hasAnnotation(final Object bean, final Class annotationClass) {
        return hasAnnotation(bean.getClass(), annotationClass);
    }

    /*unchecked*/
    public static boolean hasAnnotation(final Class targetClass, final Class annotationClass) {
        return getClassAnnotation(targetClass, annotationClass) != null;
    }

    public static <T extends Annotation> T getClassAnnotation(final Class targetClass, final Class<T> annotationClass) {
        return (T) targetClass.getAnnotation(annotationClass);
    }

    private static Map<String, Field> cache = new ConcurrentHashMap<>();

    public static Field searchForDeclaredField(Class clazz, String name) {
        var key = clazz.getTypeName() + name;
        return cache.computeIfAbsent(key, (x) -> searchInnerForDeclaredField(clazz, name));
    }

    private static Field searchInnerForDeclaredField(Class clazz, String name) {
        var result = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.getName().equals(name)).findFirst();
        if (result.isPresent()) {
            return result.get();
        }

        if (clazz.getSuperclass() == null) {
            return null;
        }
        return searchInnerForDeclaredField(clazz.getSuperclass(), name);
    }
}
