package nl.softcause.dto;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import nl.softcause.jsontemplates.definition.ILibrary;
import nl.softcause.jsontemplates.definition.TemplateDescription;
import nl.softcause.jsontemplates.nodes.INode;


@AllArgsConstructor
@Getter
public class TemplateAndDescriptionDTO {

    private TemplateDTO template;
    private TemplateDescription description;


}
