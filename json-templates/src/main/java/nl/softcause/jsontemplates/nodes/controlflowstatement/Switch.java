package nl.softcause.jsontemplates.nodes.controlflowstatement;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;

@EqualsAndHashCode(callSuper = true)
public class Switch extends ReflectionBasedNodeWithScopeImpl<Switch.SwitchScope> {

    public static Switch create(Map<String, INode[]> slots) {
        var node = new Switch();
        node.setArguments(Collections.emptyMap());
        node.setSlots(slots);
        return node;
    }

    public Switch() {
        super(SwitchScope.class);
    }

    @RequiredSlot
    @LimitSlots(allowed = {Case.class})
    private INode caseNode = null;
    private INode defaultNode = null;


    @Override
    public void internalEvaluate(TemplateModel model) {
        caseNode.evaluate(model);
        var scope = pullScopeModel(model);
        if (!scope.picked) {
            if (defaultNode != null) {
                defaultNode.evaluate(model);
            }
        }
    }

    public static class SwitchScope {
        protected boolean picked = false;
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase("Execute");
        builder.describe(getSlots().get("case"));
        builder.describeIfPresent("in case none match do", getSlots().get("defaultNode"));
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Case extends ReflectionBasedNodeImpl implements INodeWithParent<Switch> {

        @RequiredArgument
        private boolean test;
        @RequiredSlot
        private INode bodyNode;

        @JsonIgnore
        private Switch parent;

        @Override
        protected void internalEvaluate(TemplateModel model) {
            var scope = parent.pullScopeModel(model);
            if (test && !scope.picked) {
                scope.picked = true;
                parent.pushScopeModel(model, scope);
                bodyNode.evaluate(model);
            }
        }

        public static Case create(Map<String, IExpression> arguments, Map<String, INode[]> slots) {
            var node = new Case();
            node.setArguments(arguments);
            node.setSlots(slots);
            return node;
        }

        @Override
        public void registerParent(Switch parent) {
            this.parent = parent;
        }

        public Switch getRegisteredParent() {
            return parent;
        }

        @Override
        public void describe(IDescriptionBuilder builder) {
            builder.phrase().
                    add("in case").
                    expression(getArguments().
                    get("test")).
                    add("holds").
                    end();
            builder.phrase("do");
            builder.describe(getSlots().get("body"));
        }
    }

}
