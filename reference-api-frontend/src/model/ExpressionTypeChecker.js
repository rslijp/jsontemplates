import {ReturnTypes} from "../Constants";
import {getReturnType} from "./ExpressionModel";

const LOG = false;

function log(){
    // eslint-disable-next-line no-console
    if(LOG) console.log.apply(this,arguments);
}

function ExpressionTypeChecker(definition){
    const CACHE = {};
    log(definition);
    function checkTypes(expression){
        if(expression.argumentsTypes!==undefined){
            const args = expression.arguments;
            args.forEach(checkTypes);
            checkNode(expression);
        } else {
            checkSingle(expression);
        }
    }

    function checkNode(expression) {
        const argumentTypes = expression.argumentsTypes;
        const args = expression.arguments;
        if(args.length!==argumentTypes.length){
            throw "Wrong number of arguments expected "+argumentTypes.length+" but found "+args.length;
        }
        matchArgumentExpressionTypes(argumentTypes, args);
        matchGenericArguments(argumentTypes, args);
    }

    function matchArgumentExpressionTypes(argumentTypes, args) {
        for(let i=0; i<argumentTypes.length; i++){
            const expectedType = argumentTypes[i];
            matchSingleArgumentExpressionType(args[i], expectedType, (i+1).toString());
        }
    }

    function matchSingleArgumentExpressionType(argument, expectedType, name) {
        log("matchSingleArgumentExpressionType", argument, expectedType,name);
        const actualType = getExpressionType(argument);
        if(!typesMatch(expectedType, actualType)){
            throw "Type error on "+name+" expected "+cleanUpErrorType(expectedType, actualType)+" but got "+actualType;
        }
    }

    function getExpressionType(expression){
        // noinspection UnnecessaryLocalVariableJS
        const actualType = cachedDetermineReturnType(expression);
        //broken
        // if(actualType instanceof ModelDecoratedType){
        //     actualType = ((ModelDecoratedType) actualType).getUndecoratedExpressionType();
        // }
        return actualType;
    }

    function checkSingle(expression){
        cachedDetermineReturnType(expression);
    }

    function cachedDetermineReturnType(expression){
        const hit =CACHE[expression.id];
        if(hit){
            return hit;
        }
        const calc = determineReturnType(expression);
        CACHE[expression.id]=calc;
        return calc;
    }

    function determineReturnType(expression){
        const argumentExpression = expression.argumentsTypes ? expression : null;
        const type = expression.returnType(definition);
        if(baseType(type)!==ReturnTypes.GENERIC) return cleanUp(type,argumentExpression);
        if(argumentExpression != null){
            const resolvedType = findGenericArgument(argumentExpression);
            return cleanUp(resolveT(type, resolvedType), argumentExpression);
        }
        throw "Bug!";
    }

    function resolveT(genericType, resolvedType){
        log(genericType, resolvedType);
        const rawResolvedType = genericType.replace(ReturnTypes.GENERIC, baseType(resolvedType)).toLowerCase();
        return getReturnType(rawResolvedType);
    }

    function findGenericArgument(argumentExpression) {
        let resolvedType = determineReturnType(argumentExpression.arguments[0]);
        for (let i=0; i<argumentExpression.argumentsTypes.length; i++){
            const candidate = argumentExpression.argumentsTypes[i];
            if(baseType(candidate) === ReturnTypes.GENERIC){
                resolvedType = determineReturnType(argumentExpression.arguments[i]);
                break;
            }
        }
        return resolvedType;
    }

    function cleanUp(type, argumentExpression){
        type = removeOptional(type, argumentExpression);
        type = downCastIfPossible(type, argumentExpression);
        return type;
    }


    function cleanUpErrorType(expected, actual){
        if(baseType(expected)!==ReturnTypes.DECIMAL) return expected;
        if(baseType(actual)!==ReturnTypes.INTEGER) return expected;
        return isOptional(expected) ? ReturnTypes.INTEGEROPTIONAL : ReturnTypes.INTEGER;
    }

    function downCastIfPossible(type, argumentExpression){
        if(baseType(type)!==ReturnTypes.DECIMAL) return type;
        if(argumentExpression==null) return type;
        const active = argumentExpression.downCastIfPossible||false;
        if(!active) return type;
        var eraseAllowed = argumentExpression.arguments.length>0;
        for(let i=0; i<argumentExpression.arguments.length; i++){
            const argumentType = argumentExpression.argumentsTypes[i];
            const actualType = cachedDetermineReturnType(argumentExpression.arguments[i]);
            if(baseType(argumentType)===ReturnTypes.DECIMAL && baseType(actualType)!==ReturnTypes.INTEGER){
                eraseAllowed = false;
            }
        }
        if(!eraseAllowed) return type;
        return isOptional(type) ? ReturnTypes.INTEGEROPTIONAL : ReturnTypes.INTEGER;
    }



    function removeOptional(type, argumentExpression){
        if(argumentExpression==null) return type;
        const active = argumentExpression.reduceOptional||false;
        if(!active) return type;

        let eraseAllowed = argumentExpression.arguments.length>0;
        for(let i=0; i<argumentExpression.arguments.length; i++){
            const argumentType = argumentExpression.argumentsTypes[i];
            const actualType = cachedDetermineReturnType(argumentExpression.arguments[i]);
            if(isOptional(argumentType)){
                eraseAllowed &= (baseType(argumentType) === actualType);
            }
        }
        return eraseAllowed?baseType(type):type;
    }

    function typesMatch(target, candidate){
        log("target",target,"candidate",candidate);
        if(target === candidate) return true;
        if(target === ReturnTypes.DECIMAL && candidate === ReturnTypes.INTEGER) return true;
        if(baseType(target) === ReturnTypes.GENERIC && baseType(candidate) !== ReturnTypes.GENERIC) {
            return typesMatch(resolveGenericType(target, candidate), candidate);
        }
        if(isOptional(target)) {
            if(candidate===ReturnTypes.NULL) return true;
            return typesMatch(baseType(target), candidate);
        }
        return false;
    }

    function matchGenericArguments(argumentTypes, args) {
        if(argumentTypes.length===0){
            throw "Force types on zero arguments is nonsense(bug)";
        }
        let modelType=null;
        let  modelName = null;
        for(let i=0;i<argumentTypes.length;i++){
            const expectedType = argumentTypes[i];
            var argumentDefinition = cachedDetermineReturnType(args[i]);
            if(baseType(expectedType)!==ReturnTypes.GENERIC) continue;
            var currentModelType = baseType(argumentDefinition);//.getType;
            var currentModelName = argumentDefinition;//.getType();
            // if(argumentDefinition instanceof ModelDecoratedType) {
            //     currentModelType = ((ModelDecoratedType) argumentDefinition).getModelType();
            //     currentModelName = currentModelType.getSimpleName();
            // }
            if(modelType==null) {
                modelType = currentModelType;
                modelName = currentModelName;
            } else if(!primitiveTypesMatch(modelType,currentModelType)){
                throw "Wrong model expected "+modelName+" but found "+currentModelName+" at "+i;
            }
        }
    }

    function primitiveTypesMatch(target,candidate) {
        if (target===candidate) return true;
        if (target===ReturnTypes.DECIMAL && candidate===ReturnTypes.INTEGER) return true;
        return false;
    }

    function baseType(type){
        var transformed = type.replace('[]', '').replace('*', '').replace('?', '');
        return ReturnTypes[transformed.toUpperCase()];
    }

    function isOptional(type){
        return type.endsWith("?");
    }

    function resolveGenericType(genericType, candidate){
        const rawResolvedType = genericType.replace(ReturnTypes.GENERIC, baseType(candidate)).toLowerCase();
        return getReturnType(rawResolvedType);
    }

    return {
        isOptional: isOptional,
        typesMatch: typesMatch,
        resolveGenericType: resolveGenericType,
        test: (expression, expectedType, throwError) =>
        {
            log("---------");
            log("EXPRESSION", expression);
            try {
                log("RETURN", expression.returnType(definition));
            } catch(e) {
                log("RETURN", "????", e);
            }
            log("---------");
            log("EXPECTED", expectedType);
            log("---------");
            try {
                log('>>>>>>>>>START');
                checkTypes(expression);
                log('>>>>>>>>>END');
                matchSingleArgumentExpressionType(expression, expectedType, "expression");
                return {succes: true, error: null};
            } catch (e) {
                if (throwError) throw e;
                log(e);
                var suggestions  = e.getSuggestions?e.getSuggestions():null;
                return {succes: false, error: e, suggestions: suggestions};
            }
        }
    };
}

export function checkExpression (expression, definition, expectedType, throwError){
    const checker = new ExpressionTypeChecker(definition);
    if(!expression) return checker.isOptional(expectedType);
    return checker.test(expression, expectedType,throwError);
}

export function typesMatch (actualType, expectedType){
    return new ExpressionTypeChecker(null).typesMatch(expectedType, actualType);
}

export function resolveGenericType (actualType, expectedType){
    return new ExpressionTypeChecker(null).resolveGenericType(actualType, expectedType);
}