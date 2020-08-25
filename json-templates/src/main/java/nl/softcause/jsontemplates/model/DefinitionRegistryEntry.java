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
    private Object[] allowedValues;

    public IExpressionType getDecoratedType() {
        if (nested != null) {
            return Types.decorate(type, nested.getModelType());
        }
        return type;
    }

    public boolean fits(DefinitionRegistryEntry rhs) {
        if (this.equals(rhs)) {
            return true;
        }
        if (!this.name.equals(rhs.name)) {
            return false;
        }
        if (type.baseType().equals(Types.OBJECT)) {
            return false;
        }
        return Types.typesMatch(type, rhs.type);
    }
}
