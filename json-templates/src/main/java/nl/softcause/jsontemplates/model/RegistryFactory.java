package nl.softcause.jsontemplates.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nl.softcause.jsontemplates.types.TextEnumType;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import nl.softcause.jsontemplates.utils.BeanUtilsExtensions;
import org.apache.commons.beanutils.PropertyUtils;

public class RegistryFactory {

    private static final Map<Class, DefinitionRegistry> REGISTRY = new ConcurrentHashMap<>();
    private static final Collection<String> IGNORED_PROPERTIES = Arrays.asList("class", "scope");

    public static DefinitionRegistry register(Class<?> modelType) {
        if (REGISTRY.containsKey(modelType)) {
            return REGISTRY.get(modelType);
        } else {
            synchronized (REGISTRY) {
                if (REGISTRY.containsKey(modelType)) {
                    return REGISTRY.get(modelType);
                }
                DefinitionRegistry model = new DefinitionRegistry(modelType);
                REGISTRY.put(modelType, model);
                buildModel(model, modelType);
                return model;
            }
        }
    }

    private static DefinitionRegistry buildModel(DefinitionRegistry model, Class modelType) {
        var properties = PropertyUtils.getPropertyDescriptors(modelType);
        for (PropertyDescriptor p : properties) {
            extractTypeInformation(p, model, modelType);
        }
        return model.lock();
    }

    private static void extractTypeInformation(PropertyDescriptor p, DefinitionRegistry model, Class modelType) {
        var name = p.getName();
        try {
            if (ignoreProperty(p, modelType)) {
                return;
            }
            var type = Types.determine(p.getPropertyType());
            var infusedType = type.infuse(p.getPropertyType());
            var readeable = p.getReadMethod() != null;
            var writerable = p.getWriteMethod() != null;
            DefinitionRegistry nested = null;
            if (type.baseType().equals(Types.OBJECT)) {
                nested = register(BeanUtilsExtensions.resolveClass(p.getPropertyType(), true));
            }
            model.addDefintion(name, infusedType, nested, readeable, writerable, determineAllowedValues(p));
        } catch (TypeException ex) {
            throw TypeException.onProperty(ex, modelType, name);
        }
    }

    private static boolean ignoreProperty(PropertyDescriptor p, Class modelType) {
        String name = p.getName();
        var readMethod = p.getReadMethod();
        var writeMethod = p.getWriteMethod();
        if (readMethod != null && readMethod.getAnnotation(IgnoreIntrospection.class) != null) {
            return true;
        }
        if (writeMethod != null && writeMethod.getAnnotation(IgnoreIntrospection.class) != null) {
            return true;
        }
        if (IGNORED_PROPERTIES.contains(name)) {
            return true;
        }
        try {
            Field field = modelType.getDeclaredField(name);
            if (field.getAnnotation(IgnoreIntrospection.class) != null) {
                return true;
            }
        } catch (NoSuchFieldException | SecurityException e) {
//
        }
        return false;
    }

    private static Object[] determineAllowedValues(PropertyDescriptor p) {
        if (p.getPropertyType().isEnum()) {
            return TextEnumType.getEnumValues(p.getPropertyType()).toArray();
        }
        return null;
    }
}
