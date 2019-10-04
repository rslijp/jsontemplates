package nl.softcause.jsontemplates.definition;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class ModelDescription {

    private List<ModelPropertyDescription> propertyDescriptions;

    ModelDescription(int id, String type) {
        this.id=id;
        this.type=type;
    }

    private int id;

    private final String type;

    void add(ModelPropertyDescription modelDescription) {
        if (propertyDescriptions == null) propertyDescriptions = new ArrayList<>();
        propertyDescriptions.add(modelDescription);
    }

    @Override
    public String toString() {
        return String.format("%s as ref %d",type, id);
    }
}
