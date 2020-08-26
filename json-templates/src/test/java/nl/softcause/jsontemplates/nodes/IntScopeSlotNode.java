package nl.softcause.jsontemplates.nodes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;

@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class IntScopeSlotNode extends ReflectionBasedNodeWithScopeImpl<IntScopeSlotNode.Scope> {

    @SingleSlot
    public INode mainNode;

    public IntScopeSlotNode() {
        super(IntScopeSlotNode.Scope.class);
    }

    public static IntScopeSlotNode create(String into, INode... children) {
        var node = new IntScopeSlotNode();
        node.setSlots(Map.of("main", children));
        node.setArguments(Map.of("into", new Constant(into)));
        return node;
    }

    @SuppressWarnings("unused")
    private String into;

    @Override
    protected void internalEvaluate(TemplateModel model)
    {
        var scope = pullScopeModel(model);
        scope.setValue(10);
        pushScopeModel(model, scope);
        if(mainNode!=null) mainNode.evaluate(model);
        scope = pullScopeModel(model);
        model.set(into, scope.value);
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase("Single slot with int scope");
        builder.describe(mainNode);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Scope {
        int value=0;
    }
}
