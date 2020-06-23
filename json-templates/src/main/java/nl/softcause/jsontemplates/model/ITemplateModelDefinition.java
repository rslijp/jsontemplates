package nl.softcause.jsontemplates.model;

import nl.softcause.jsontemplates.types.IExpressionType;

public interface ITemplateModelDefinition extends IModelDefinition {

    Class getModelType();

    void pushScope(Object owner);

    void popScope();

    ScopeModel scope();

    void addDefinition(String name, IExpressionType type, DefinitionRegistry nested, boolean readeable,
                       boolean writerable, Object defaultValue, Object[] allowedValues);
}
