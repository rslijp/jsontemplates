package nl.softcause.jsontemplates.definition;

import lombok.Getter;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.UUID;

@Getter
class ModelPropertyDescription {



    ModelPropertyDescription(String name, IExpressionType type, boolean readable, boolean writable) {
        this.name=name;
        this.type=type.getType();
        this.readable=readable;
        this.writable=writable;
    }

    private String name;
    private final String type;
    private final boolean writable;
    private final boolean readable;
    private Integer modelReference;

    @Override
    public String toString() {
        var operation = (readable?"get":"") +(readable&&writable?",":"")+ (writable?"set":"");
        return String.format("%s %s -> %s",operation, name, type+(modelReference!=null?String.format("(%s)",modelReference):""));
    }

    public void addRef(int reference) {
        this.modelReference=reference;
    }
}
