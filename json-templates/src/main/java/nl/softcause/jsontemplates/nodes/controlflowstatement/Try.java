package nl.softcause.jsontemplates.nodes.controlflowstatement;

import lombok.*;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;

import java.util.Collections;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class Try extends ReflectionBasedNodeWithScopeImpl<Try.TryScope> {


    public static Try create(Map<String, INode[]> slots) {
        var node = new Try();
        node.setArguments(Collections.emptyMap());
        node.setSlots(slots);
        return node;
    }

    protected Try(){
        super(Try.TryScope.class);
    }

    @RequiredSlot
    private INode bodyNode=null;
    private INode onErrorNode=null;

    @Override
    public void internalEvaluate(TemplateModel model) {
        try {
            bodyNode.evaluate(model);
        } catch (Exception e){
            pushScopeModel(model, new TryScope( e.getMessage(),e));
            if(onErrorNode!=null) onErrorNode.evaluate(model);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TryScope {
        protected String errorMessage;
        protected Exception exception;
    }

}
