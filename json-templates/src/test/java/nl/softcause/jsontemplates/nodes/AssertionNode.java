package nl.softcause.jsontemplates.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import lombok.Data;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.ITemplateModelDefinition;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.types.ISlotPattern;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class AssertionNode implements INode {

    private final Map<String, ArgumentDefinition> argumentsTypes = Collections.emptyMap();
    private final Map<String, ISlotPattern> slotTypes = Collections.emptyMap();
    private String errorMessage = null;
    private Map<String, IExpression> arguments;
    private Map<String, INode[]> slots;
    private int counter;
    private Function<TemplateModel, Void> validation;

    public AssertionNode() {
    }

    public AssertionNode throwErrorOnEvaluate(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public AssertionNode validate(Function<TemplateModel, Void> validation) {
        this.validation = validation;
        return this;
    }

    @Override
    public void registerDefinitions(ITemplateModelDefinition model) {

    }

    @Override
    public void evaluate(TemplateModel model) {
        counter++;
        if (errorMessage != null) {
            throw new RuntimeException(errorMessage);
        }
        if (validation != null) {
            validation.apply(model);
        }
    }

    @JsonIgnore
    public boolean isCalled() {
        return counter > 0;
    }

    @Override
    public void revokeDefinitions(ITemplateModelDefinition model) {

    }

    @Override
    public void describe(IDescriptionBuilder builder) {
        builder.phrase("Assertion");
        builder.phrase()
                .add("Counter is")
                .expression(new Constant(counter))
                .end();
    }
}
