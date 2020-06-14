package nl.softcause.wizard;

import java.util.Collections;
import java.util.Map;
import lombok.EqualsAndHashCode;
import nl.softcause.dialogs.Dialog;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.MultiNode;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;

@EqualsAndHashCode(callSuper = true)
public class Wizard extends ReflectionBasedNodeWithScopeImpl<Wizard.WizardScope> {

    Wizard(){
        super(Wizard.WizardScope.class);
    }

    @RequiredSlot
    @LimitSlots(allowed={Wizard.Step.class})
    private INode stepsNode=null;

    public static Wizard create(Step...steps) {
        var wizard = new Wizard();
        wizard.stepsNode = new MultiNode(steps);
        return wizard;
    }

    @Override
    protected void internalEvaluate(TemplateModel model) {

    }

    public static class WizardScope {
        protected boolean gandalf=false;
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Step extends ReflectionBasedNodeImpl implements INodeWithParent<Wizard> {

        public static Step create(IExpression applicable, Dialog dialog) {
            var step = new Step();
            step.setArguments(Collections.singletonMap("applicable", applicable));
            step.stepsNode = dialog;
            return step;
        }


        @RequiredSlot
        @LimitSlots(allowed={Dialog.class})
        private INode stepsNode=null;

        private boolean applicable;


        @Override
        public void registerParent(Wizard parent) {

        }

        @Override
        protected void internalEvaluate(TemplateModel model) {

        }
    }
}
