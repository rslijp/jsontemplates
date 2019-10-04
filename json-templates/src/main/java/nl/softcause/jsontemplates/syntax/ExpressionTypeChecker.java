package nl.softcause.jsontemplates.syntax;

import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.IExpressionWithArguments;
import nl.softcause.jsontemplates.expressions.ReduceOptionalAnnotation;
import nl.softcause.jsontemplates.model.IModelDefinition;
import nl.softcause.jsontemplates.model.ModelDefinition;
import nl.softcause.jsontemplates.model.ModelException;
import nl.softcause.jsontemplates.types.*;
import nl.softcause.jsontemplates.utils.ClassUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpressionTypeChecker {

    private Map<IExpression, IExpressionType> CACHE = new ConcurrentHashMap<>();

    private final IModelDefinition definition;

    public ExpressionTypeChecker(IModelDefinition definition){
        this.definition=definition;
    }

    public <T> ExpressionTypeChecker(Class<T> type){
        this.definition=new ModelDefinition<T>(type);
    }


    public void checkTypes(IExpression expression){
        if(expression instanceof IExpressionWithArguments){
            var expressionWithArguments = (IExpressionWithArguments) expression;
            var arguments = expressionWithArguments.getArguments();
            arguments.stream().forEach(this::checkTypes);
            checkNode(expressionWithArguments);
        } else {
            checkSingle(expression);
        }
    }

    private void checkSingle(IExpression expression) {
        try {
            cachedDetermineReturnType(expression);
        } catch (ModelException e){
            throw TypeCheckException.from(e);
        } catch (TypeException e){
            throw TypeCheckException.from(e);
        }
    }

    private void checkNode(IExpressionWithArguments expression) {
        var argumentTypes = expression.getArgumentsTypes();
        var arguments = expression.getArguments();
        if(arguments.size()!=argumentTypes.length){
            throw TypeCheckException.wrongNumberOfArguments(argumentTypes.length, arguments.size());
        }

        matchArgumentExpressionTypes(argumentTypes, arguments);
        matchGenericArguments(argumentTypes, arguments);
    }

    private void matchArgumentExpressionTypes(IExpressionType[] argumentTypes, List<IExpression> arguments) {
        for(var i=0;i<argumentTypes.length;i++){
            var expectedType = argumentTypes[i];
            matchSingleArgumentExpressionType(arguments.get(i), expectedType, String.valueOf(i+1));
        }
    }

    public void matchSingleArgumentExpressionType(IExpression argument,  IExpressionType expectedType, String name) {
        var actualType = getExpressionType(argument);
        if(!Types.typesMatch(expectedType, actualType)){
            throw TypeCheckException.typeError(expectedType, actualType, name);
        }
    }

    public IExpressionType getExpressionType(IExpression expression){
        var actualType = cachedDetermineReturnType(expression);
        if(actualType instanceof ModelDecoratedType){
            actualType = ((ModelDecoratedType) actualType).getUndecoratedExpressionType();
        }
        return actualType;
    }

    public IExpressionType getRawExpressionType(IExpression expression){
        return cachedDetermineReturnType(expression);
    }

    IExpressionType cachedDetermineReturnType(IExpression expression) {
        return CACHE.computeIfAbsent(expression, (exp)->determineReturnType(exp));
    }

    private IExpressionType determineReturnType(IExpression expression) {
        var argumentExpression = expression instanceof IExpressionWithArguments ? (IExpressionWithArguments) expression : null;

        var reduceOptional = ClassUtil.hasAnnotation(expression, ReduceOptionalAnnotation.class);
        var type = expression.getReturnType(definition);
        if(!type.baseType().equals(Types.GENERIC)) return removeOptional(reduceOptional,type,argumentExpression);
        if(argumentExpression != null){
            IExpressionType resolvedType = findGenericArgument(argumentExpression);
            return removeOptional(reduceOptional, GenericType.resolveT(type, resolvedType), argumentExpression);
        }
        throw new RuntimeException("Bug!");
    }

    private IExpressionType findGenericArgument(IExpressionWithArguments argumentExpression) {
        var resolvedType = determineReturnType(argumentExpression.getArguments().get(0));
        for (var i=0; i<argumentExpression.getArgumentsTypes().length; i++){
            var candidate = argumentExpression.getArgumentsTypes()[i];
            if(candidate.baseType()== Types.GENERIC){
                resolvedType = determineReturnType(argumentExpression.getArguments().get(i));
                break;
            }
        }
        return resolvedType;
    }

    private IExpressionType removeOptional(boolean active, IExpressionType type, IExpressionWithArguments argumentExpression){
        if(!active) return type;
        if(argumentExpression==null) return type;
        var eraseAllowed = argumentExpression.getArguments().size()>0;
        for(var i=0; i<argumentExpression.getArguments().size(); i++){
            var argumentType = argumentExpression.getArgumentsTypes()[i];
            var actualType = cachedDetermineReturnType(argumentExpression.getArguments().get(i));
            if(argumentType instanceof Optional){
                eraseAllowed &= (argumentType.baseType().equals(actualType));
            }
        }
        return eraseAllowed?type.baseType():type;
    }

    private void matchGenericArguments(IExpressionType[] argumentTypes, List<IExpression> arguments) {
        if(argumentTypes.length==0){
            throw new RuntimeException("Force types on zero arguments is nonsense(bug)");
        }
        Class<?> modelType=null;
        String modelName = null;
        for(var i=0;i<argumentTypes.length;i++){
            var expectedType = argumentTypes[i];
            var argumentDefinition = cachedDetermineReturnType(arguments.get(i));
            if(!expectedType.baseType().equals(Types.GENERIC)) continue;
            var currentModelType = argumentDefinition.baseType().baseType().getClass();
            var currentModelName = argumentDefinition.getType();
            if(argumentDefinition instanceof ModelDecoratedType) {
                currentModelType = ((ModelDecoratedType) argumentDefinition).getModelType();
                currentModelName = currentModelType.getSimpleName();
            }
            if(modelType==null) {
                modelType = currentModelType;
                modelName = currentModelName;
            } else if(modelType!=currentModelType){
                throw TypeCheckException.wrongModel(modelName, currentModelName,i);
            }
        }
    }

}
