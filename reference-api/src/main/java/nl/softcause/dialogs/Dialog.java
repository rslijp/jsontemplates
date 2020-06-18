package nl.softcause.dialogs;

import java.util.Map;
import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.base.MultiNode;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeWithScopeImpl;
import nl.softcause.jsontemplates.types.IExpressionType;

@EqualsAndHashCode(callSuper = true)
public class Dialog extends ReflectionBasedNodeWithScopeImpl<Dialog.DialogScope> {

    Dialog(){
        super(Dialog.DialogScope.class);
    }

    public static Dialog create(Dialog.Field... fields){
        var dialog = new Dialog();
        dialog.inputNode=new MultiNode(fields);
        return dialog;
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

        public static Field create(String name, IExpressionType<?> type, boolean required) {
            var field = new Field();
            field.setArguments(Map.of(
                    "field", new Constant("'"+name+"'"),
                    "type", new Constant("'"+type.getType()+"'"),
                    "required", new Constant(required)
            ));
            return field;
        }

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
