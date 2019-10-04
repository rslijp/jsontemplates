package nl.softcause.jsontemplates.expressions;

import nl.softcause.jsontemplates.model.DefinitionRegistry;
import nl.softcause.jsontemplates.model.DefinitionRegistryEntry;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Types;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.ModelException;

import java.util.HashMap;
import java.util.Locale;

public class TestModel implements IModel {

    private HashMap<String, Object> model = new HashMap<>();

    @Override
    public DefinitionRegistryEntry getDefinition(String name) {
        return new DefinitionRegistryEntry(name, Types.determineConstant(get(name)), null, true, true);
    }

    @Override
    public DefinitionRegistryEntry[] getDefinitions() {
        return new DefinitionRegistryEntry[0];
    }

    @Override
    public Object get(String name) {
        if(!model.containsKey(name)) throw ModelException.notFound(name, this.getClass());
        return model.get(name);
    }

    @Override
    public void set(String name, Object value) {
        model.put(name, value);
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    public TestModel put(String name, Object value) {
        set(name, value);
        return this;
    }
}
