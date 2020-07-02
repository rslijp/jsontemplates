package nl.softcause.jsontemplates.nodes;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public interface INode {

    @JsonIgnore
    Map<String, ArgumentDefinition> getArgumentsTypes();

    @JsonInclude
    Map<String, IExpression> getArguments();

    @JsonIgnore
    Map<String, ISlotPattern> getSlotTypes();

    @JsonInclude
    Map<String, INode[]> getSlots();

    void registerDefinitions(ITemplateModelDefinition model);

    void evaluate(TemplateModel model);

    void revokeDefinitions(ITemplateModelDefinition model);

    void describe(IDescriptionBuilder builder);
}
