package nl.softcause.wizard;

import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;
import nl.softcause.jsontemplates.nodes.controlflowstatement.Switch;
import nl.softcause.nl.softcause.dialogs.Dialog;

@EqualsAndHashCode(callSuper = true)
public class Wizard extends ReflectionBasedNodeWithScopeImpl<Wizard.WizardScope> {

    Wizard(){
        super(Wizard.WizardScope.class);
    }

    @RequiredSlot
    @LimitSlots(allowed={Wizard.Step.class})
    private INode stepsNode=null;

    @Override
    protected void internalEvaluate(TemplateModel model) {

    }

    public static class WizardScope {
        protected boolean gandalf=false;
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Step extends ReflectionBasedNodeImpl implements INodeWithParent<Wizard> {

        @RequiredSlot
        @LimitSlots(allowed={Dialog.class})
        private INode stepsNode=null;


        @Override
        public void registerParent(Wizard parent) {

        }

        @Override
        protected void internalEvaluate(TemplateModel model) {

        }
    }
}
