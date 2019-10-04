package nl.softcause.jsontemplates.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.softcause.jsontemplates.types.IExpressionType;

import java.util.List;

public interface IExpressionWithArguments extends IExpression {

    @JsonIgnore
    IExpressionType[] getArgumentsTypes();

    List<IExpression> getArguments();


}
