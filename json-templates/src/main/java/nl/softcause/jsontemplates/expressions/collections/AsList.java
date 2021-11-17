package nl.softcause.jsontemplates.expressions.collections;

import static nl.softcause.jsontemplates.types.Types.LIST_OBJECT;
import static nl.softcause.jsontemplates.types.Types.OBJECT;
import static nl.softcause.jsontemplates.types.Types.OPTIONAL_OBJECT;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import nl.softcause.jsontemplates.OperatorPrecendence;
import nl.softcause.jsontemplates.collections.ObjectList;
import nl.softcause.jsontemplates.expressions.ExpressionParseType;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.model.IModel;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.types.IExpressionType;


@Value
public class AsList implements IExpressionWithArguments {

    @JsonInclude
    private java.util.List<IExpression> arguments;

    @JsonIgnore
    private final IExpressionType[] argumentsTypes =
            new IExpressionType[] {OPTIONAL_OBJECT, OPTIONAL_OBJECT, OPTIONAL_OBJECT, OPTIONAL_OBJECT, OPTIONAL_OBJECT};

    @Override
    public IExpressionType getReturnType(IModelDefinition model) {
        return LIST_OBJECT;
    }

    @Override
    public Object evaluate(IModel model) {
        var list = new ObjectList();
        for (var i = 0; i < getArguments().size(); i++) {
            list.add(OBJECT.convert(getArguments().get(i).evaluate(model)));
        }
        return list;
    }

    @Override
    public Integer priority() {
        return OperatorPrecendence.FUNCTION;
    }


    @Override
    public ExpressionParseType parseType() {
        return ExpressionParseType.FUNCTION;
    }


}
