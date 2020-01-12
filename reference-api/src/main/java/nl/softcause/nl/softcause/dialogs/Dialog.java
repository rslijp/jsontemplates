package nl.softcause.nl.softcause.dialogs;

import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;

@EqualsAndHashCode(callSuper = true)
public class Dialog extends ReflectionBasedNodeWithScopeImpl<Dialog.DialogScope> {

    Dialog(){
        super(Dialog.DialogScope.class);
    }

    @RequiredSlot
    @LimitSlots(allowed={Dialog.Field.class})
    private INode inputNode=null;

    @Override
    protected void internalEvaluate(TemplateModel model) {

    }

    public static class DialogScope {
        protected boolean monolog=false;
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Field extends ReflectionBasedNodeImpl implements INodeWithParent<Dialog> {

        @Override
        public void registerParent(Dialog parent) {
            
        }

        @RequiredArgument
        private String field;

        private String devaultValue;

        @DefaultValue(value=0L)
        private boolean required;

        @RequiredArgument
        private String type;


        @Override
        protected void internalEvaluate(TemplateModel model) {

        }
    }
}
