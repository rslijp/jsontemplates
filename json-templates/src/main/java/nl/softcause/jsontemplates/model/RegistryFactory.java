package nl.softcause.jsontemplates.model;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            var type = Types.determine(p.getPropertyType());
//            var infusedType = type.infuse(p.getPropertyType());
            var readeable = p.getReadMethod() != null;
            var writerable = p.getWriteMethod() != null;
            if (IGNORED_PROPERTIES.contains(name)) {
                return;
            }
            DefinitionRegistry nested = null;
            if (type.baseType().equals(Types.OBJECT)) {
                nested = register(BeanUtilsExtensions.resolveClass(p.getPropertyType(), true));
            }
            model.addDefintion(name, type, nested, readeable, writerable);
        } catch (TypeException ex) {
            throw TypeException.onProperty(ex, modelType, name);
        }
    }
}
