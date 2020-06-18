package nl.softcause.jsontemplates.model;

import java.util.Locale;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class DefinedModel<T> implements IModel {


    private final ModelDefinition<T> defintion;
    private T subject = null;

    @Getter
    @Setter
    private Locale locale;

    public DefinedModel(@NonNull Class<T> modelType) {
        this.defintion = new ModelDefinition<>(modelType);
    }

    public DefinedModel(@NonNull T subject) {
        this.defintion = new ModelDefinition<>((Class<T>) subject.getClass());
        load(subject);
    }

    @Override
    public DefinitionRegistryEntry getDefinition(@NonNull String name) {
        return defintion.getDefinition(name);
    }

    @Override
    public DefinitionRegistryEntry[] getDefinitions() {
        return defintion.getDefinitions();
    }


    @Override
    public Object get(String name) {
        return defintion.get(subject, name);
    }

    @Override
    public void set(String name, Object value) {
        defintion.set(subject, name, value);
    }

    public void load(T subject) {
        this.subject = subject;
    }

    public ModelDefinition<T> getModelDefinition() {
        return defintion;
    }

    public Class getModelType() {
        return this.defintion.getModelType();
    }
}
