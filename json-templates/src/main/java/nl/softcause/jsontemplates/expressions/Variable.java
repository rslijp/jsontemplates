package nl.softcause.jsontemplates.expressions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Value;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;
import nl.softcause.jsontemplates.types.TypeException;

import java.beans.ConstructorProperties;

@Value
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public class Variable implements IExpression {

    private Variable(){this(null);}

    @ConstructorProperties({"name"})
    public Variable(String name){
        this.name=name;
    }

    @JsonInclude
    private String name;

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        if(model== null){
            throw TypeException.noModelDefinition(name);
        }
        return model.getDefinition(name).getDecoratedType();
    }

    @Override
    public Object evaluate(IModel model) {
        return getReturnType(model).convert(model.get(name));
    }

    @Override
    public Integer priority() {
        return null;
    }

    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.VARIABLE;
    }

    public String operator(){
        return "$";
    }

    public String toString(){
        return name;
    }
}
