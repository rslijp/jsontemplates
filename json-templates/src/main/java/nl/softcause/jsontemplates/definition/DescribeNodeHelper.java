package nl.softcause.jsontemplates.definition;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.IScopeChange;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;

public class DescribeNodeHelper {

    private static final boolean LOG = false;
    private final Class[] mainNodes;

    DescribeNodeHelper(Class[] mainNodes) {
        this.mainNodes = mainNodes;
    }

    void describe(TemplateDescription description) {
        var seen = new HashMap<Class, NodeDescription>();
        log("NODES");
        for (var entry : mainNodes) {
            description.addMainNode(describeNode(entry, description, seen));
        }
        seen.values().forEach(description::add);
    }

    private long describeNode(Class nodeClass, TemplateDescription template, Map<Class, NodeDescription> seen) {
        if (seen.containsKey(nodeClass)) {
            return seen.get(nodeClass).getId();
        }
        var description =
                new NodeDescription(template.newNodeId(), nodeClass.getSimpleName(), nodeClass.getPackageName());
        seen.put(nodeClass, description);
        INode node = createNode(nodeClass);
        collectSubNodes(nodeClass, template, seen);
        node.getSlotTypes().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(
                slot -> {
                    var pattern = slot.getValue();
                    description.addSlot(slot.getKey(), pattern, seen);
                }
        );
        node.getArgumentsTypes().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(
                slot -> {
                    description.addArgument(slot.getKey(), slot.getValue().getType());
                }
        );
        if (node instanceof IScopeChange) {
            var changeSet = ((IScopeChange) node).getScopeChange();
            if (changeSet == null || changeSet.isEmpty()) {
                description.addEmptyScope();
            } else {
                changeSet.forEach(description::addScope);
            }
        }
        log(description.toString());
        return description.getId();
    }

    private void collectSubNodes(Class nodeClass, TemplateDescription template, Map<Class, NodeDescription> seen) {
        var fields = nodeClass.getDeclaredFields();
        var newDefined = new HashSet<Class<?>>();
        for (var field : fields) {
            var fieldType = field.getType();
            if (INode.class.isAssignableFrom(fieldType)) {
                var limit = field.getAnnotation(ReflectionBasedNodeImpl.LimitSlots.class);
                if (limit != null) {
                    Arrays.stream(limit.allowed()).forEach(newDefined::add);
                }
            }
        }
        newDefined.stream().sorted(Comparator.comparing(Class::getSimpleName))
                .forEach(c -> describeNode(c, template, seen));

    }

    private static INode createNode(Class entry) {
        try {
            var constructor = entry.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (INode) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("BROKEN");
        }
    }

    private void log(String line) {
        if (LOG) {
            System.out.println(line);
        }
    }
}
