package nl.softcause.jsontemplates.nodes.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.SneakyThrows;
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
import nl.softcause.jsontemplates.types.TextEnumType;
import nl.softcause.jsontemplates.types.Types;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@SuppressWarnings("unchecked")
public abstract class ReflectionBasedNodeImpl implements INode {

    private static ThreadLocal<Stack<INode>> parentsContext = new ThreadLocal<>();

    private final Class nodeClass;
    protected Map<String, IExpression> arguments = new LinkedHashMap<>();
    protected Map<String, INode[]> slots;

    @JsonIgnore
    private final Map<String, ArgumentDefinition> argumentsTypes = new LinkedHashMap<>();

    @JsonIgnore
    private final Map<String, ISlotPattern> slotTypes = new LinkedHashMap<>();

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
        reflectOnNode(nodeClass);
    }

    private void reflectOnNode(Class clazz) {
        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
            var fieldType = field.getType();
            if (field.getAnnotation(IgnoreArgument.class) != null) {
                continue;
            }
            if (INode.class.isAssignableFrom(fieldType)) {
                reflectOnNodeFIeld(field);
            } else {
                reflectOnArgumentField(field, fieldType);
            }
        }
        //Check super
        if (shouldRecurse(clazz)) {
            reflectOnNode(clazz.getSuperclass());
        }
    }

    private boolean shouldRecurse(Class clazz) {
        return !clazz.getSuperclass().equals(ReflectionBasedNodeWithScopeImpl.class) &&
                !clazz.getSuperclass().equals(ReflectionBasedNodeImpl.class);
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
                type = type.infuse(field.getType());
                defaultValue = type.convert(field.getAnnotation(DefaultValue.class).value());
            }
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
        var parent = getParent();
        pushParent();
        this.registerDefinitions(model);
        populateFields(model, parent);
        internalEvaluate(model);
        this.revokeDefinitions(model);
        popParent();
    }

    private INode getParent() {
        var stack = parentsContext.get();
        if (stack == null || stack.size() == 0) {
            return null;
        }
        return stack.peek();
    }

    private void pushParent() {
        var stack = parentsContext.get();
        if (stack == null) {
            stack = new Stack<>();
            parentsContext.set(stack);
        }
        stack.push(this);
    }

    private void popParent() {
        var stack = parentsContext.get();
        stack.pop();
    }

    private void populateFields(TemplateModel model, INode parent) {
        populateFields(nodeClass, model, parent);
    }

    private void populateFields(Class clazz, TemplateModel model, INode parent) {
        var fields = clazz.getDeclaredFields();
        if(this instanceof INodeWithParent<?>) {
            ((INodeWithParent<INode>) this).registerParent(parent);
        }
        for (var field : fields) {
            populateField(model, field);
        }

        if (shouldRecurse(clazz)) {
            populateFields(clazz.getSuperclass(), model, parent);
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
                var value = getArguments().get(fieldName).evaluate(model);
                if (field.getType().isEnum() && value instanceof String) {
                    value = TextEnumType.getEnumValue(field.getType(), (String) value);
                }
                if (value != null && field.getAnnotation(AllowedValues.class) != null) {
                    guardFieldValue(value, fieldName, model, field.getAnnotation(AllowedValues.class));
                }
                field.set(this, value);
            } else {
                field.set(this, getArgumentsTypes().get(fieldName).getDefaultValue());
            }
        } catch (IllegalAccessException e) {
            throw ReflectionBasedNodeException.illegalAccessOfArgumentField(nodeClass, fieldName);
        }
    }

    @SneakyThrows
    private void guardFieldValue(Object value, String fieldName, TemplateModel model, AllowedValues annotation) {
        var argumentDefinition = getArgumentsTypes().get(fieldName);
        var contextField = annotation.contextField();
        var discriminatorField = annotation.discriminatorField();
        var values = annotation.factory().getConstructor().newInstance().valuesFor(
                getDiscriminatorValue(contextField, model),
                getDiscriminatorValue(discriminatorField, model)
        );
        var allowed = values.stream().map(v -> argumentDefinition.getType().convert(v)).collect(Collectors.toList());
        if (!allowed.contains(value)) {
            throw ReflectionBasedNodeException.illegalValueFor(fieldName, value, allowed);
        }
    }

    protected String getDiscriminatorValue(String field, TemplateModel model) {
        String fullPath = field;
        if (StringUtils.isBlank(field)) {
            return null;
        }
        INode c = this;
        var parts = new ArrayList(Arrays.asList(field.split("\\.")));
        while (parts.get(0).equals("parent")) {
            var withParent = (INodeWithParent<?>) c;
            c = withParent.getRegisteredParent();
            parts.remove(0);
            if (c == null) {
                throw new IllegalArgumentException("Failed to resolve parent of " + fullPath);
            }
        }
        field = String.join(".", parts);
        var nodeField = c.getArguments().get(field);
        if (nodeField == null) {
            throw new IllegalArgumentException("Failed to resolve field " + fullPath + " on node");
        }
        var value = c.getArguments().get(field).evaluate(model);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    private void populateNodeField(TemplateModel model, Field field) {
        var fieldName = field.getName();
        try {
            if (this instanceof INodeWithParent && fieldName.equals("parent")) {
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
    public @interface IgnoreArgument {
    }


    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiredArgument {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultValue {
        String value();
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

}
