package nl.softcause.jsontemplates.definition;

import lombok.Getter;
import lombok.Setter;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.model.NodeScopeChange;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.nodes.types.LimitedSlot;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.*;

@Getter
public class NodeDescription {

    public NodeDescription(long id, String name){
        this.id=id;
        this.name=name;
    }

    private long id;

    private String name;
    private Map<String,String> argumentTypes;
    private Map<String,String> nodeSlots;
    private Map<String,long[]> nodeSlotLimits;
    private Map<String,NodeScopeDescription> scopeChanges;

    void addArgument(String name, IExpressionType argType) {
        if(argumentTypes==null) argumentTypes = new HashMap<>();
        argumentTypes.put(name,argType.getType());
    }


    void addSlot(String name, ISlotPattern pattern, Map<Class, NodeDescription> lookup) {
        if(nodeSlots==null) nodeSlots = new HashMap<>();
        var description = pattern.getDescription();
        if(pattern.getBasePattern() instanceof LimitedSlot){
            if(nodeSlotLimits==null) nodeSlotLimits = new HashMap<>();
            description="limited";
            var limits = ((LimitedSlot) pattern).getLimit();
            var limitDescription = new long[limits.length];
            for(var i=0; i<limits.length;i++){
                var limit = limits[i];
                limitDescription[i]=lookup.get(limit).getId();
            }
            nodeSlotLimits.put(name, limitDescription);
        }
        nodeSlots.put(name,description);
    }

    void addScope(NodeScopeChange scopeChange) {
        if(scopeChanges==null) addEmptyScope();
        scopeChanges.put(scopeChange.getName(),new NodeScopeDescription(
                scopeChange.getType().getType(),
                scopeChange.isWritable())
        );
    }


    public void addEmptyScope() {
        scopeChanges = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" as ");
        sb.append(id);
        sb.append(":");

        if(nodeSlots!=null) {
            sb.append(" slots=");
            var first = true;
            for (var slot : nodeSlots.entrySet()) {
                if(!first) sb.append(", ");
                sb.append("[" + slot.getKey() + "] -> " + slot.getValue());
                first = false;
            }
        }
        if(argumentTypes!=null) {
            sb.append(" arguments=");
            var first = true;
            for (var slot : argumentTypes.entrySet()) {
                if(!first) sb.append(", ");
                sb.append("@"+slot.getKey() + " -> " + slot.getValue());
                first=false;
            }
        }

        if(scopeChanges!=null) {
            sb.append(" scope=");
            var first = true;
            for (var scope : scopeChanges.entrySet()) {
                if(!first) sb.append(", ");
                sb.append("+" + scope.getKey() + ": " + scope.getValue());
                first = false;
            }
        }

        return sb.toString();
    }

}
