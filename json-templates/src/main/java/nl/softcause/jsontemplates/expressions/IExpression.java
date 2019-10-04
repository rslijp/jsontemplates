package nl.softcause.jsontemplates.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public interface IExpression {

    @JsonIgnore
    IExpressionType getReturnType(IModelDefinition model);

    Object evaluate(@NonNull IModel model);

    @JsonIgnore
    Integer priority();

    @JsonIgnore
    ExpressionParseType parseType();
}
