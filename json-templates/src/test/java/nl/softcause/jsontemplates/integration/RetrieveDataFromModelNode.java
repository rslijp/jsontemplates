package nl.softcause.jsontemplates.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import nl.softcause.jsontemplates.collections.ObjectList;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.model.TemplateModel;
import nl.softcause.jsontemplates.nodes.IDescriptionBuilder;
import nl.softcause.jsontemplates.nodes.base.NamingField;
import nl.softcause.jsontemplates.nodes.base.ReflectionBasedNodeImpl;
import org.apache.commons.lang3.reflect.MethodUtils;

public class RetrieveDataFromModelNode extends ReflectionBasedNodeImpl {

    @NamingField
    @RequiredArgument
    private Object source;

    @RequiredArgument
    private String methodName;

    private ObjectList methodArguments;

    @RequiredArgument
    private String target;

    public static RetrieveDataFromModelNode create(IExpression source, String methodName,
                                                        IExpression methodArguments, String target) {
        var node = new RetrieveDataFromModelNode();
        Map<String, IExpression> arguments = new HashMap<>();
        arguments.put("source", (IExpression) source);
        arguments.put("methodName", (IExpression) new Constant(methodName));
        arguments.put("target", (IExpression) new Constant(target));
        arguments.put("methodArguments", (IExpression) methodArguments);
        node.setArguments(arguments);
        return node;
    }



    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    @Override
    protected void internalEvaluate(TemplateModel model) {
        if (source == null) {
            throw new RuntimeException(String.format("No source", source));
        }
        var arguments = methodArguments.toArray();
        var argumentTypes = Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new);
        var result = MethodUtils.invokeMethod(source, methodName, arguments, argumentTypes);
        model.set(target, result);
    }

    @Override
    public void describe(IDescriptionBuilder builder) {
    }
}
