package nl.softcause.dto;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Data;
import nl.softcause.jsontemplates.definition.ILibrary;
import nl.softcause.jsontemplates.expresionparser.ExpressionFormatter;
import nl.softcause.jsontemplates.expresionparser.ExpressionParser;
import nl.softcause.jsontemplates.nodes.INode;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;
import org.apache.commons.lang3.StringUtils;

@Data
public class NodeDTO implements Serializable {

    private String name;

    private Map<String, String> arguments = new HashMap<>();

    private Map<String, NodeDTO[]> slots = new HashMap<>();

    public NodeDTO() {
    }

    public static NodeDTO asDTO(INode node) {
        var dto = new NodeDTO();
        dto.setName(node.getClass().getSimpleName());
        var formatter = new ExpressionFormatter();
        node.getArguments().entrySet().stream().forEach(entry -> {
            var expression = entry.getValue();
            if (expression == null) {
                return;
            }
            dto.arguments.put(entry.getKey(), formatter.format(expression));
        });
        node.getSlots().entrySet().stream().forEach(entry -> {
            var slots = entry.getValue();
            if (slots == null) {
                return;
            }
            dto.slots.put(entry.getKey(), Arrays.stream(slots).map(NodeDTO::asDTO).toArray(NodeDTO[]::new));
        });
        return dto;
    }

    public INode asTemplate(ILibrary library) {
        Optional<Class> nodeClass = library.getNodeClass(getName());
        if (nodeClass.isEmpty()) {
            throw new IllegalArgumentException("Not supported " + getName());
        }
        INode node = null;
        try {
            node = (INode) nodeClass.get().getConstructor().newInstance();

        } catch (Exception e) {
            throw new IllegalArgumentException("Oops " + getName());
        }
        var parser = new ExpressionParser();
        final INode finalNode = node;
        arguments.entrySet().stream().forEach(entry -> {
            var raw = entry.getValue();
            if (StringUtils.isEmpty(raw)) {
                return;
            }
            finalNode.getArguments().put(entry.getKey(), parser.parse(raw));
        });
        slots.entrySet().stream().forEach(entry -> {
            var raw = entry.getValue();
            if (raw == null) {
                return;
            }
            String slotFieldName = toSlotName(entry.getKey());
            ISlotPattern slotPattern = finalNode.getSlotTypes().get(slotFieldName);
            if (slotPattern == null) {
                throw new IllegalArgumentException(MessageFormat
                        .format("Node {0} doesn''t have slot(or field) named {1}", getName(), slotFieldName));
            }
            var limit = slotPattern.getLimit();
            if (limit != null) {
                library.push(limit);
            }
            finalNode.getSlots().put(entry.getKey(),
                    Arrays.stream(raw).map(c -> c.asTemplate(library)).toArray(INode[]::new)
            );
            if (limit != null) {
                library.pop();
            }
        });
        return node;
    }

    public static String toSlotName(String slotName) {
        if (slotName.endsWith("Node")) {
            return slotName;
        }
        return slotName + "Node";
    }
}
