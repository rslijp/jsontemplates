package nl.softcause.jsontemplates.model;

import lombok.Value;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.Types;

@Value
public class DefinitionRegistryEntry {
    private String name;
    private IExpressionType type;
    private DefinitionRegistry nested;
    private boolean readable;
    private boolean writable;

    public IExpressionType getDecoratedType() {
        if (nested != null) {
            return Types.decorate(type, nested.getModelType());
        }
        return type;
    }

}
