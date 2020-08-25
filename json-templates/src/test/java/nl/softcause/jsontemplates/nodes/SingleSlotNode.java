package nl.softcause.jsontemplates.nodes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class SingleSlotNode extends ReflectionBasedNodeImpl {

    @SingleSlot
    public INode mainNode;

    public static SingleSlotNode create(INode... children) {
        var node = new SingleSlotNode();
        node.setSlots(Map.of("main", children));
        return node;
    }

    @Override
    protected void internalEvaluate(TemplateModel model) {
        mainNode.evaluate(model);
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase("Single slot");
        builder.describe(mainNode);
    }
}
