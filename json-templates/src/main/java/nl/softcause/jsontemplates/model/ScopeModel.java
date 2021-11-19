package nl.softcause.jsontemplates.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Types;


public class ScopeModel implements IModel {

    @Getter
    private final Object owner;

    @Setter
    @Getter
    public Locale locale;

    private Map<String, DefinitionRegistryEntry> definitions = new HashMap<>();

    private Map<String, Object> values = new HashMap<>();
    private Map<String, Object> defaultValues = new HashMap<>();


    public ScopeModel(Object owner) {
        this.owner = owner;
    }

    public void addNodeScopeChange(NodeScopeChange change) {
        this.addDefintion(change.getName(), change.getType(), null, true, change.isWritable(),
                change.getDefaultValue(), change.getAllowedValues());
    }

    public void addDefintion(@NonNull String name, @NonNull IExpressionType type, DefinitionRegistry nested,
                             boolean readable, boolean writable, Object defaultValue, Object[] allowedValues) {
        guardNesting(name);
        var defaultType = Types.determineConstant(defaultValue);
        var newDefinition = new DefinitionRegistryEntry(name, type, nested, readable, writable, allowedValues);
        if (definitions.containsKey(name) && !definitions.get(name).fits(newDefinition)) {
            if (!newDefinition.fits(definitions.get(name))) {
                throw ScopeException.alreadyDefined(name);
            }
            definitions.put(name, newDefinition);
        }
        if (defaultValue != null && !Types.runtimeTypesMatch(type, defaultType)) {
            throw ScopeException.defaultValueTypeError(name, type, defaultType);
        }
        if ((type.baseType().equals(Types.OBJECT) || type.baseType().equals(Types.GENERIC)) && defaultValue != null &&
                nested != null) {
            if (!nested.getModelType().isInstance(defaultValue)) {
                throw ScopeException.defaultValueTypeError(name, nested.getModelType(), defaultValue.getClass());

            }
        }
        definitions.put(name, newDefinition);
//        values.put(name, type.convert(defaultValue));
        if (defaultValue != null) {
            defaultValues.put(name, type.convert(defaultValue));
        }
    }

    public void dropDefintion(String name) {
        guardNesting(name);
        if (!definitions.containsKey(name)) {
            throw ScopeException.notDefined(name);
        }
        definitions.remove(name);
    }

    public void clear() {
        definitions.clear();
    }

    public boolean hasDefinition(@NonNull String name) {
        var parts = name.split(TemplateModel.SEPARATOR);
        var localName = parts[0];
        if (!definitions.containsKey(localName)) {
            return false;
        }
        var def = definitions.get(localName);
        if (parts.length > 1) {
            if (def.getNested() == null) {
                return false;
            }
            var relativeName = String.join(TemplateModel.SEPARATOR_CHAR, Arrays.copyOfRange(parts, 1, parts.length));
            def = def.getNested().getDefinition(relativeName);
        }
        return def != null;
    }

    @Override
    public DefinitionRegistryEntry getDefinition(@NonNull String name) {
        var parts = name.split(TemplateModel.SEPARATOR);
        var localName = parts[0];
        if (!definitions.containsKey(localName)) {
            throw ScopeException.notFound(localName);
        }
        var def = definitions.get(localName);
        if (parts.length > 1) {
            if (def.getNested() == null) {
                throw ScopeException.nestedDefinitionNotAllowed(name);
            }
            var relativeName = String.join(TemplateModel.SEPARATOR_CHAR, Arrays.copyOfRange(parts, 1, parts.length));
            def = def.getNested().getDefinition(relativeName);
        }
        return def;
    }

    @Override
    public DefinitionRegistryEntry[] getDefinitions() {
        return definitions.values().stream().sorted(Comparator.comparing(DefinitionRegistryEntry::getName))
                .toArray(DefinitionRegistryEntry[]::new);
    }

    private void guardNesting(String name) {
        if (name.contains(TemplateModel.SEPARATOR_CHAR)) {
            throw ScopeException.nestedDefinitionNotAllowed(name);
        }
    }


    @Override
    public Object get(@NonNull String name) {
        var parts = name.split(TemplateModel.SEPARATOR);
        var localName = parts[0];

        var def = getDefinition(localName);
        if (def.isReadable()) {
            var rawValue = values.containsKey(localName) ? values.get(localName) : defaultValues.get(localName);
            if (!Types.isNullable(def.getType()) && rawValue == null) {
                throw ScopeException.defaultValueTypeError(name, def.getType(), Types.NULL);
            }
            var value = def.getType().convert(rawValue);
            if (parts.length > 1) {
                if (def.getNested() == null) {
                    throw ScopeException.nestedDefinitionNotAllowed(name);
                }
                var relativeName =
                        String.join(TemplateModel.SEPARATOR_CHAR, Arrays.copyOfRange(parts, 1, parts.length));
                value = def.getNested().read(value, relativeName);
            }
            return value;
        } else {
            throw ScopeException.notReadable(name);
        }
    }

    public void setLocal(@NonNull String localName, Object value) {
        var def = getDefinition(localName);
        values.put(localName, def.getType().convert(value));
    }

    @Override
    public void set(@NonNull String name, Object value) {
        var parts = name.split(TemplateModel.SEPARATOR);
        var localName = parts[0];

        var def = getDefinition(localName);
        if (parts.length > 1) {
            if (def.isReadable()) {
                var src = def.getType().convert(values.get(localName));
                if (def.getNested() == null) {
                    throw ScopeException.nestedDefinitionNotAllowed(name);
                }
                var relativeName =
                        String.join(TemplateModel.SEPARATOR_CHAR, Arrays.copyOfRange(parts, 1, parts.length));
                def.getNested().write(src, relativeName, value);
            } else {
                throw ScopeException.notReadable(name);
            }
        } else {
            if (def.isWritable()) {
                values.put(name, def.getType().convert(value));
            } else {
                throw ScopeException.notWritable(name);
            }
        }
    }

}
