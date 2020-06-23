package nl.softcause.jsontemplates.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.Getter;
import nl.softcause.jsontemplates.collections.BeanConverters;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;
import nl.softcause.jsontemplates.types.Types;
import nl.softcause.jsontemplates.utils.BeanUtilsExtensions;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

public class DefinitionRegistry {

    private static Pattern REPLACE_INDEX = Pattern.compile(
            "((\\[[0-9]+\\])|(\\([a-zA-Z0-9àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ \\(\\)\\.,'\\-]+\\)))");
    private static Pattern ENDS_WITH_INDEX = Pattern.compile(
            "((\\[[0-9]+\\])|(\\([a-zA-Z0-9àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ \\(\\)\\.,'\\-]+\\)))$");

    static {
        BeanConverters.register();
    }

    private Map<String, DefinitionRegistryEntry> model;

    @Getter
    private Class<?> modelType;

    DefinitionRegistry(Class<?> modelType) {
        this.modelType = modelType;
        this.model = new HashMap<>();
    }

    DefinitionRegistry lock() {
        model = Collections.unmodifiableMap(model);
        return this;
    }

    void addDefintion(String name, IExpressionType type, DefinitionRegistry nested, boolean readeable,
                      boolean writerable) {
        model.put(name, new DefinitionRegistryEntry(name, type, nested, readeable, writerable));
    }

    DefinitionRegistryEntry getDefinition(String name) {
        var chunkIndex = name.indexOf(TemplateModel.SEPARATOR_CHAR);
        var part = chunkIndex > -1 ? name.substring(0, chunkIndex) : name;
        if (!model.containsKey(part)) {
            throw ModelException.notFound(part, modelType);
        }
        DefinitionRegistryEntry definition = model.get(part);
        if (chunkIndex > -1) {
            if (definition.getType().baseType() != Types.OBJECT) {
                throw ModelException.primitiveCantHaveProperty(definition.getType(), part, name);
            }
            return definition.getNested().getDefinition(name.substring(chunkIndex + 1));
        }
        return definition;
    }


    Object read(Object source, String name) {
        var nameWithOutIndexer = REPLACE_INDEX.matcher(name).replaceAll("");
        var endsWithIndex = ENDS_WITH_INDEX.asPredicate().test(name);
        var definition = getDefinition(nameWithOutIndexer);
        if (!definition.isReadable()) {
            throw ModelException.notReadeable(name, modelType);
        }
        try {
            var value = PropertyUtils.getProperty(source, name);
            var type = definition.getType();
            if (endsWithIndex) {
                type = type.baseType();
            }
            return type.convert(value);
        } catch (IllegalAccessException e) {
            throw ModelException.noAccess(name, modelType);
        } catch (InvocationTargetException e) {
            throw ModelException.invocationErrorDuringGet(name, modelType, e);
        } catch (NoSuchMethodException e) {
            throw ModelException.noSuchGetMethod(name, modelType);
        } catch (NestedNullException e) {
            throw ModelException.nestedNullModel(name);
        } catch (NullPointerException e) {
            throw ModelException.nestedNullModel(name);
        }
    }

    void write(Object source, String name, Object value) {
        var nameWithOutIndexer = REPLACE_INDEX.matcher(name).replaceAll("");
        var endsWithIndex = ENDS_WITH_INDEX.asPredicate().test(name);
        var definition = getDefinition(nameWithOutIndexer);
        if (!definition.isWritable()) {
            throw ModelException.notWriteable(name, modelType);
        }
        try {
            var type = definition.getType();
            if (endsWithIndex) {
                type = type.baseType();
            }
            var propertyDescriptor = PropertyUtils.getPropertyDescriptor(source, name);
            var propertyType = BeanUtilsExtensions.resolveClass(propertyDescriptor.getPropertyType(), endsWithIndex);
            Object converted = convert(value, type, propertyType);
            PropertyUtils.setProperty(source, name, converted);
        } catch (NullPointerException e) {
            throw ModelException.nestedNullModel(name);
        } catch (IllegalAccessException e) {
            throw ModelException.noAccess(name, modelType);
        } catch (IllegalArgumentException | InvocationTargetException e) {
            throw ModelException.invocationErrorDuringSet(name, modelType, e);
        } catch (NoSuchMethodException e) {
            throw ModelException.noSuchGetMethod(name, modelType);
        }
    }

    private Object convert(Object value, IExpressionType type, Class propertyType) {
        var infusedType = type.infuse(propertyType); //to handle type safe enums
        Object converted = value;
        try {
            if (value != null && propertyType != value.getClass() && infusedType.isA(value)) {
                converted = infusedType.convert(value);
            }
        } catch (TypeException Te) {
            //jammer dit...ff naar kijken.
        }
        if (converted != null && converted.getClass() != propertyType) {
            converted = ConvertUtils.convert(converted, propertyType);
        }
        return converted;
    }


    @Override
    public String toString() {
        return modelType.getTypeName();
    }

    public DefinitionRegistryEntry[] getDefinitions() {
        return model.values().stream().sorted(Comparator.comparing(DefinitionRegistryEntry::getName))
                .toArray(DefinitionRegistryEntry[]::new);
    }
}
