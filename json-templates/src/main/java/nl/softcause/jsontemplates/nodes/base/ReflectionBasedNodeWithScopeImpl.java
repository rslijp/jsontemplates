package nl.softcause.jsontemplates.nodes.base;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.softcause.jsontemplates.model.*;
import nl.softcause.jsontemplates.nodes.IScopeChange;
import nl.softcause.jsontemplates.types.TextEnumType;
import nl.softcause.jsontemplates.types.Types;
import org.apache.commons.beanutils.ConvertUtils;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unchecked")
public abstract class ReflectionBasedNodeWithScopeImpl<T> extends ReflectionBasedNodeImpl implements IScopeChange {

    private final Class<T> scopeModel;

    protected ReflectionBasedNodeWithScopeImpl(Class<T> scopeModel) {
        this.scopeModel = scopeModel;
    }

    @Override
    public void registerDefinitions(ITemplateModelDefinition model) {
        model.pushScope(this);
        reflectOnScope(model);
    }

    private void reflectOnScope(ITemplateModelDefinition model) {
        var scope = model.scope();
        getScopeChange().forEach(scope::addNodeScopeChange);
    }

    public List<NodeScopeChange> getScopeChange() {
        var scopeChanges = new ArrayList<NodeScopeChange>();
        var fields = getScopeFields();
        try {
            var constructor = scopeModel.getDeclaredConstructor();
            var instance = constructor.newInstance();
            for (var field : fields) {
                scopeChanges.add(collectOnScopeField(instance, field));
            }
        } catch (NoSuchMethodException NSe) {
            throw ReflectionBasedNodeException.scopeMissedDefaultConstructor(scopeModel);
        } catch (IllegalAccessException IAe) {
            throw ReflectionBasedNodeException.illegalAccessOfScopeConstructor(scopeModel);
        } catch (InvocationTargetException | InstantiationException IAe) {
            throw ReflectionBasedNodeException.errorInvokingScopeConstructor(scopeModel, IAe);
        }
        return scopeChanges;
    }


    private NodeScopeChange collectOnScopeField(T instance, Field field) {
        var fieldName = field.getName();
        var fieldType = field.getType();
        try {
            var writable = field.canAccess(instance);
            field.setAccessible(true);
            return new NodeScopeChange(fieldName, Types.determine(fieldType), writable, field.get(instance),
                    determineAllowedValues(field));
        } catch (IllegalAccessException IAe) {
            throw ReflectionBasedNodeException.illegalGetAccessOfScopeField(scopeModel, fieldName);
        }

    }

    protected Object[] determineAllowedValues(Field field) {
        if (field.getType().isEnum()) {
            return TextEnumType.getEnumValues(field.getType()).toArray();
        }
        return null;
    }


    private List<Field> getScopeFields() {
        var result = new ArrayList<Field>();
        var fields = scopeModel.getDeclaredFields();
        for (var field : fields) {
            var fieldType = field.getType();
            if (fieldType == this.getClass()) {
                continue;
            }
            result.add(field);
        }
        return result;
    }

    public T pullScopeModel(TemplateModel model) {
        var fields = getScopeFields();
        try {
            var constructor = scopeModel.getDeclaredConstructor();
            var instance = constructor.newInstance();
            for (var field : fields) {
                pullScopeField(model, instance, field);
            }
            return instance;
        } catch (NoSuchMethodException NSe) {
            throw ReflectionBasedNodeException.scopeMissedDefaultConstructor(scopeModel);
        } catch (IllegalAccessException IAe) {
            throw ReflectionBasedNodeException.illegalAccessOfScopeConstructor(scopeModel);
        } catch (InvocationTargetException | InstantiationException IAe) {
            throw ReflectionBasedNodeException.errorInvokingScopeConstructor(scopeModel, IAe);
        }

    }

    private void pullScopeField(TemplateModel model, T instance, Field field) {
        var fieldName = field.getName();
        try {
            field.setAccessible(true);
            Optional<ScopeModel> scope = model.scope(this);
            if (scope.isEmpty()) {
                throw ScopeException.notFoundForOwner(this);
            }
            var value = scope.get().get(fieldName);
            var fieldType = field.getType();
            if (value != null && Types.isPrimitive(Types.determine(fieldType))) {
                value = ConvertUtils.convert(value, fieldType);
            }
            field.set(instance, value);
        } catch (IllegalAccessException IAe) {
            throw ReflectionBasedNodeException.illegalSetAccessOfScopeField(scopeModel, fieldName);
        }
    }

    protected void pushScopeModel(TemplateModel model, T scopeInstance) {
        var fields = getScopeFields();
        for (var field : fields) {
            pushScopeField(model, scopeInstance, field);
        }
    }

    private void pushScopeField(TemplateModel model, T scopeInstance, Field field) {
        var fieldName = field.getName();
        try {
            field.setAccessible(true);
            model.scope().setLocal(fieldName, field.get(scopeInstance));
        } catch (IllegalAccessException IAe) {
            throw ReflectionBasedNodeException.illegalGetAccessOfScopeField(scopeModel, fieldName);
        }
    }

    @Override
    public void revokeDefinitions(ITemplateModelDefinition model) {
        model.popScope();
    }
}
