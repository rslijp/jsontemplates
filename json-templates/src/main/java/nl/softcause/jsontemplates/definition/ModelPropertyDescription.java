package nl.softcause.jsontemplates.definition;

import lombok.Getter;
import nl.softcause.jsontemplates.types.IExpressionType;

@Getter
class ModelPropertyDescription {


    ModelPropertyDescription(String name, IExpressionType type, boolean readable, boolean writable, Object[] allowedValues) {
        this.name = name;
        this.type = type.getType();
        this.readable = readable;
        this.writable = writable;
        this.allowedValues = allowedValues;
    }

    private String name;
    private final String type;
    private final boolean writable;
    private final boolean readable;
    private Integer modelReference;
    private Object[] allowedValues;

    @Override
    public String toString() {
        var operation = (readable ? "get" : "") + (readable && writable ? "," : "") + (writable ? "set" : "");
        return String.format("%s %s -> %s", operation, name,
                type + (modelReference != null ? String.format("(%s)", modelReference) : ""));
    }

    public void addRef(int reference) {
        this.modelReference = reference;
    }
}
