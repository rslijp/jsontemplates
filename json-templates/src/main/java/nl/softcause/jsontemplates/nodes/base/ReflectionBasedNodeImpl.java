package nl.softcause.jsontemplates.nodes.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.ArgumentDefinition;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.INodeWithParent;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.nodes.types.LimitedSlot;
import nl.softcause.jsontemplates.nodes.types.OptionalSlot;
import nl.softcause.jsontemplates.nodes.types.WildCardSlot;
import nl.softcause.jsontemplates.types.Types;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@SuppressWarnings("unchecked")
public abstract class ReflectionBasedNodeImpl implements INode {


    private final Class nodeClass;
    protected Map<String, IExpression> arguments = new HashMap<>();
    protected Map<String, INode[]> slots;

    @JsonIgnore
    private final Map<String, ArgumentDefinition> argumentsTypes = new HashMap<>();

    @JsonIgnore
    private final Map<String, ISlotPattern> slotTypes = new HashMap<>();

    public ReflectionBasedNodeImpl() {
        this.nodeClass = this.getClass();
        reflectOnNode();

        setSlots(new LinkedHashMap<>());

    }
//
//    public void setSlots(Map<String, INode[]> slots){
//        var listeningSlots =new ListenerMap<>(slots);
//        PropertyChangeListener propertyChangeListener = evt -> {
//        };
//        listeningSlots.addPropertyChangeListener(propertyChangeListener);
//        this.slots=listeningSlots;
//    }

    private void reflectOnNode() {
        var fields = nodeClass.getDeclaredFields();

        for (var field : fields) {
            var fieldType = field.getType();
            if (INode.class.isAssignableFrom(fieldType)) {
                reflectOnNodeFIeld(field);
            } else {
                reflectOnArgumentField(field, fieldType);
            }
        }
    }

    private void reflectOnArgumentField(Field field, Class<?> fieldType) {
        var fieldName = field.getName();
        try {
            var type = Types.determine(fieldType);
            field.setAccessible(true);
            if (field.getAnnotation(RequiredArgument.class) != null) {
                type = Types.byName(type.getType().replace("?", ""));
            }

            Object defaultValue = field.get(this);
            if (field.getAnnotation(DefaultValue.class) != null) {
                defaultValue = field.getAnnotation(DefaultValue.class).value();
            }
//            String objectPath = null;
//            if(type.baseType() == Types.OBJECT && field.getAnnotation(TypeFromModel.class)!=null){
//                objectPath = field.getAnnotation(TypeFromModel.class).path();
//            }
            getArgumentsTypes().put(fieldName, new ArgumentDefinition(type, defaultValue/*,objectPath*/));
        } catch (IllegalAccessException IAe) {
            throw ReflectionBasedNodeException.illegalAccessOfArgumentField(nodeClass, fieldName);
        }
    }

    private void reflectOnNodeFIeld(Field field) {
        if (field.getAnnotation(JsonIgnore.class) != null) {
            return;
        }
        var fieldName = field.getName();
        var required = field.getAnnotation(RequiredSlot.class) != null;
        var limit = field.getAnnotation(LimitSlots.class);
        ISlotPattern slotPattern = limit != null ? new LimitedSlot(limit.allowed()) : new WildCardSlot();
        getSlotTypes().put(fieldName, required ? slotPattern : new OptionalSlot(slotPattern));
    }


    public void evaluate(TemplateModel model) {
        this.registerDefinitions(model);
        populateFields(model);
        internalEvaluate(model);
        this.revokeDefinitions(model);
    }

    private void populateFields(TemplateModel model) {
        var fields = nodeClass.getDeclaredFields();

        for (var field : fields) {
            populateField(model, field);
        }
    }

    protected void populateField(TemplateModel model, String fieldName) {
        Field field;
        try {
            field = nodeClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw ReflectionBasedNodeException.noSuchField(nodeClass, fieldName);
        }
        populateField(model, field);

    }

    private void populateField(TemplateModel model, Field field) {
        field.setAccessible(true);
        var fieldType = field.getType();
        if (INode.class.isAssignableFrom(fieldType)) {
            populateNodeField(model, field);
        } else {
            populateArgumentField(model, field);
        }
    }

    private void populateArgumentField(TemplateModel model, Field field) {
        var fieldName = field.getName();
        try {
            if (getArguments().get(fieldName) != null) {
                field.set(this, getArguments().get(fieldName).evaluate(model));
            } else {
                field.set(this, getArgumentsTypes().get(fieldName).getDefaultValue());
            }
        } catch (IllegalAccessException e) {
            throw ReflectionBasedNodeException.illegalAccessOfArgumentField(nodeClass, fieldName);
        }
    }

    private void populateNodeField(TemplateModel model, Field field) {
        var fieldName = field.getName();
        try {
            if (this instanceof INodeWithParent && fieldName.equals("parent")) {
                field.set(this, model.scope().getOwner());
                return;
            }
            fieldName = NodeUtil.slotName(fieldName);
            field.set(this, new MultiNode(getSlots().get(fieldName)));
        } catch (IllegalAccessException e) {
            throw ReflectionBasedNodeException.illegalAccessOfNodeField(nodeClass, fieldName);
        }
    }

    protected abstract void internalEvaluate(TemplateModel model);


    @Override
    public void registerDefinitions(ITemplateModelDefinition model) {
    }


    @Override
    public void revokeDefinitions(ITemplateModelDefinition model) {
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == null) {
            return false;
        }
        if (!rhs.getClass().equals(nodeClass)) {
            return false;
        }
        var rhsNode = (ReflectionBasedNodeImpl) rhs;
        if (!arguments.equals(rhsNode.arguments)) {
            return false;
        }
        if (slots.size() != rhsNode.slots.size()) {
            return false;
        }
        for (var entry : slots.entrySet()) {
            INode[] lhsSlots = slots.get(entry.getKey());
            INode[] rhsSlots = rhsNode.slots.get(entry.getKey());
            if (!Arrays.equals(lhsSlots, rhsSlots)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiredArgument {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultValue {
        long value();
    }


    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiredSlot {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LimitSlots {
        Class[] allowed();
    }

//    @Target({ElementType.FIELD})
//    @Retention(RetentionPolicy.RUNTIME)
//    public @interface TypeFromModel { String path();}

}
