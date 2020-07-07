package nl.softcause.jsontemplates.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.SneakyThrows;
import nl.softcause.jsontemplates.model.NodeScopeChange;
import nl.softcause.jsontemplates.nodes.base.AllowedValues;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import nl.softcause.jsontemplates.nodes.types.LimitedSlot;
import nl.softcause.jsontemplates.types.AllowedValueSets;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TextEnumType;

@Getter
public class NodeDescription {

    public NodeDescription(long id, String name, String packageName) {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
    }

    private long id;

    private String name;
    private String packageName;
    private Map<String, String> argumentTypes;
    private Map<String, AllowedValuesDescription> allowedValues;
    private Map<String, String> nodeSlots;
    private Map<String, long[]> nodeSlotLimits;
    private Map<String, NodeScopeDescription> scopeChanges;

    void addArgument(String field, IExpressionType argType) {
        if (argumentTypes == null) {
            argumentTypes = new LinkedHashMap<>();
        }
        argumentTypes.put(field, argType.getType());
    }

    @SneakyThrows
    void addAllowedValueSet(String field, AllowedValues definition) {
        if (allowedValues == null) {
            allowedValues = new LinkedHashMap<>();
        }
        var factory = definition.factory().getConstructor().newInstance();
        allowedValues
                .put(field, new AllowedValuesDescription(name, definition.discriminatorField(), factory.allValues()));
    }

    void addEnumAllowedValueSet(String field, Class enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Expected an enum");
        }
        if (allowedValues == null) {
            allowedValues = new LinkedHashMap<>();
        }
        var valueSet = new AllowedValueSets(null, new ArrayList<>(TextEnumType.getEnumValues(enumClass)));
        var description = new AllowedValuesDescription(name, null, Collections.singletonList(valueSet));
        allowedValues.put(field, description);
    }


    void addSlot(String name, ISlotPattern pattern, Map<Class, NodeDescription> lookup) {
        if (nodeSlots == null) {
            nodeSlots = new LinkedHashMap<>();
        }
        var description = pattern.getDescription();
        if (pattern.getBasePattern() instanceof LimitedSlot) {
            if (nodeSlotLimits == null) {
                nodeSlotLimits = new LinkedHashMap<>();
            }
            description = "limited";
            var limits = pattern.getLimit();
            var limitDescription = new long[limits.length];
            for (var i = 0; i < limits.length; i++) {
                var limit = limits[i];
                limitDescription[i] = lookup.get(limit).getId();
            }
            nodeSlotLimits.put(name, limitDescription);
        }
        nodeSlots.put(name, description);
    }

    void addScope(NodeScopeChange scopeChange) {
        if (scopeChanges == null) {
            addEmptyScope();
        }
        scopeChanges.put(scopeChange.getName(), new NodeScopeDescription(
                scopeChange.getType().getType(),
                scopeChange.isWritable())
        );
    }


    void addEmptyScope() {
        scopeChanges = new LinkedHashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" as ");
        sb.append(id);
        sb.append(":");

        if (nodeSlots != null) {
            sb.append(" slots=");
            var first = true;
            for (var slot : nodeSlots.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("[").append(slot.getKey()).append("] -> ").append(slot.getValue());
                first = false;
            }
        }
        if (argumentTypes != null) {
            sb.append(" arguments=");
            var first = true;
            for (var slot : argumentTypes.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("@").append(slot.getKey()).append(" -> ").append(slot.getValue());
                first = false;
            }
        }

        if (scopeChanges != null) {
            sb.append(" scope=");
            var first = true;
            for (var scope : scopeChanges.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("+").append(scope.getKey()).append(": ").append(scope.getValue());
                first = false;
            }
        }

        return sb.toString();
    }

}
