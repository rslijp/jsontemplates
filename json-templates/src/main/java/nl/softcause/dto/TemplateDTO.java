package nl.softcause.dto;

import java.io.Serializable;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.softcause.jsontemplates.definition.ILibrary;
import nl.softcause.jsontemplates.nodes.INode;

@Data
@AllArgsConstructor
public class TemplateDTO implements Serializable {

    private NodeDTO[] slots;

    public TemplateDTO() {
    }

    public static TemplateDTO asDTO(INode[] slots) {
        var dto = new TemplateDTO();
        dto.slots = Arrays.stream(slots).map(NodeDTO::asDTO).toArray(NodeDTO[]::new);
        return dto;
    }

    public INode[] asTemplate(ILibrary library) {
        return Arrays.stream(slots).map(slot -> slot.asTemplate(library)).toArray(INode[]::new);
    }

    public TemplateDTO clone() {
        return new TemplateDTO(Arrays.copyOf(slots, slots.length));
    }
}
