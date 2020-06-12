package nl.softcause.jsontemplates.model;

public interface IModelDefinition {

    DefinitionRegistryEntry getDefinition(String name);

    DefinitionRegistryEntry[] getDefinitions();

}
