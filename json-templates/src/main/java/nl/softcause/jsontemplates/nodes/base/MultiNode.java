package nl.softcause.jsontemplates.nodes.base;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Value;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;

@Value
public class MultiNode implements INode {
    @JsonIgnore
    private INode[] nodes;

    public MultiNode(INode[] nodes) {
        this.nodes = nodes;
        this.slots = Collections.singletonMap("children", nodes);
    }

    @Getter
    protected Map<String, IExpression> arguments = Collections.emptyMap();
    @Getter
    protected Map<String, INode[]> slots;

    @Getter
    @JsonIgnore
    private final Map<String, ArgumentDefinition> argumentsTypes = Collections.emptyMap();

    @Getter
    @JsonIgnore
    private final Map<String, ISlotPattern> slotTypes = Collections.singletonMap("children", new WildCardSlot());


    @Override
    public void evaluate(TemplateModel model) {
        if(nodes == null) {
            return;
        }
        for (INode node : nodes) {
            node.evaluate(model);
        }
    }

    @Override
    public void registerDefinitions(ITemplateModelDefinition model) {
        for (INode node : nodes) {
            node.registerDefinitions(model);
        }
    }

    @Override
    public void revokeDefinitions(ITemplateModelDefinition model) {
        for (INode node : nodes) {
            node.revokeDefinitions(model);
        }
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == null) {
            return false;
        }
        if (!(rhs instanceof MultiNode)) {
            return false;
        }
        var rhsMultiNode = (MultiNode) rhs;
        if (nodes.length != rhsMultiNode.nodes.length) {
            return false;
        }
        for (var i = 0; i < nodes.length; i++) {
            if (!this.nodes[i].equals(rhsMultiNode.nodes[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (INode node : nodes) {
            hashCode += node.hashCode();
        }
        return hashCode;
    }
}
