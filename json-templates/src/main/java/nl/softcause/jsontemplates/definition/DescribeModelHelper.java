package nl.softcause.jsontemplates.definition;

import java.util.HashMap;
import java.util.Map;

import nl.softcause.jsontemplates.model.DefinitionRegistryEntry;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.types.Types;

public class DescribeModelHelper {

    private static final boolean LOG = false;
    private final ITemplateModelDefinition definition;

    DescribeModelHelper(ITemplateModelDefinition definition) {
        this.definition = definition;
    }

    void describe(TemplateDescription description) {
        var seen = new HashMap<String, ModelDescription>();
        log("MODEL");
        describeSubModel(description.getClass(), definition.getDefinitions(), description, seen);
        seen.values().forEach(description::add);
    }

    private ModelPropertyDescription describeModelProperty(DefinitionRegistryEntry entry, TemplateDescription template,
                                                           Map<String, ModelDescription> seen) {
        var description = new ModelPropertyDescription(
                entry.getName(),
                entry.getType(),
                entry.isReadable(),
                entry.isWritable(),
                entry.getAllowedValues());
        if (entry.getNested() != null && entry.getType().baseType() == Types.OBJECT) {
            var nested = entry.getNested();
            description.addRef(describeSubModel(nested.getModelType(), nested.getDefinitions(), template, seen));
        }
        log("\t" + description.toString());
        return description;
    }

    private int describeSubModel(Class modelType, DefinitionRegistryEntry[] entries, TemplateDescription template,
                                 Map<String, ModelDescription> seen) {
        String key = modelType.getName();
        if (seen.containsKey(key)) {
            return seen.get(key).getId();
        }
        var description = new ModelDescription(template.newModelId(), key);
        seen.put(key, description);
        log(description.toString());
        for (var entry : entries) {
            description.add(describeModelProperty(entry, template, seen));
        }
        return description.getId();
    }

    private void log(String line) {
        if (LOG) {
            System.out.println(line);
        }
    }

}
