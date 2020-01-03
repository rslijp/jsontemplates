package nl.softcause.jsontemplates.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TemplateDescription {

    @Getter
    private Set<Long> mainNodeIds;
    @Getter
    private Integer mainModelId;
    @Getter
    private List<ModelDescription> modelDescriptions;
    @Getter
    private List<ExpressionDescription> expressionDescriptions;
    @Getter
    private List<NodeDescription> nodeDescriptions;

    void add(ExpressionDescription expressionDescription){
        if(expressionDescriptions==null) expressionDescriptions= new ArrayList<>();
        expressionDescriptions.add(expressionDescription);
    }

    @JsonIgnore
    private int expressionId=0;

    int newExpressionId() {
        return ++expressionId;
    }

    void add(NodeDescription nodeDescription) {
        if(nodeDescriptions==null) nodeDescriptions= new ArrayList<>();
        nodeDescriptions.add(nodeDescription);
    }

    @JsonIgnore
    private int nodeId=0;

    int newNodeId() {
        return ++nodeId;
    }

    void add(ModelDescription modelDescription) {
        if (modelDescriptions == null) modelDescriptions = new ArrayList<>();
        if(mainModelId==null) mainModelId=modelDescription.getId();
        modelDescriptions.add(modelDescription);
    }


    @JsonIgnore
    private int modelId=0;

    int newModelId() {
        return ++modelId;
    }

    void addMainNode(long nodeId) {
        if (mainNodeIds == null) mainNodeIds = new HashSet<>();
        mainNodeIds.add(nodeId);
    }
}
