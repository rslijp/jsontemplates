package nl.softcause.jsontemplates.expressions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.softcause.jsontemplates.types.IExpressionType;

public interface IExpressionWithArguments extends IExpression {

    @JsonIgnore
    IExpressionType[] getArgumentsTypes();

    List<IExpression> getArguments();


}
