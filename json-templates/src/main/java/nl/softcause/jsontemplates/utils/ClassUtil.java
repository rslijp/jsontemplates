package nl.softcause.jsontemplates.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import nl.softcause.jsontemplates.expressions.IExpression;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

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

    public static Class[] listAllExpressions() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
        );
        return reflections.getSubTypesOf(IExpression.class)
                .stream()
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .toArray(Class[]::new);
    }


}
