package nl.softcause.jsontemplates.model;

import lombok.Getter;
import lombok.NonNull;
import nl.softcause.jsontemplates.types.Types;

public class ModelDefinition<T> implements IModelDefinition {

    @Getter
    private final Class<T> modelType;

    private DefinitionRegistry model;

    public ModelDefinition(Class<T> modelType) {
        this.modelType = modelType;
        model = RegistryFactory.register(modelType);
    }

//    void addDefinition(String name, IExpressionType type, DefinitionRegistry nested, boolean readeable, boolean writerable) {
//        model.addDefinition(name,type,nested,readeable,writerable);
//    }

    @Override
    public DefinitionRegistryEntry getDefinition(@NonNull String name) {
        var parts = name.split("\\.");
        var c = model;
        for (var i = 0; i < parts.length; i++) {
            var definition = c.getDefinition(parts[i]);
            if (i == parts.length - 1) {
                return definition;
            } else if (definition.getType().baseType() != Types.OBJECT) {
                throw ModelException.primitiveCantHaveProperty(definition.getType(), parts[i], name);
            }
        }
        throw new RuntimeException("bug");
    }

    @Override
    public DefinitionRegistryEntry[] getDefinitions() {
        return model.getDefinitions();
    }

    Object get(T subject, String name) {
        if (subject == null) {
            throw ModelException.noModelLoaded();
        }
        return model.read(subject, name);
    }

    void set(T subject, String name, Object value) {
        if (subject == null) {
            throw ModelException.noModelLoaded();
        }
        model.write(subject, name, value);
    }

}
