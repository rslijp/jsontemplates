package nl.softcause.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.softcause.jsontemplates.definition.TemplateDescription;


@AllArgsConstructor
@Getter
public class TemplateAndDescriptionDTO {

    private TemplateDTO template;
    private TemplateDescription description;
    private String commitUrl;
    private String cancelUrl;

}
