import _ from "underscore";
import {ReturnTypes} from "../Constants";
import {getReturnType} from "./ExpressionModel";

function ExpressionTypeChecker(definition){
    const CACHE = {};
    console.log(definition);
    function checkTypes(expression){
        if(expression.argumentsTypes!==undefined){
            var args = expression.arguments;
            _.forEach(args, checkTypes);
            checkNode(expression);
        } else {
            checkSingle(expression);
        }
    }

    function checkNode(expression) {
        var argumentTypes = expression.argumentsTypes;
        var args = expression.arguments;
        if(args.length!==argumentTypes.length){
            throw "Wrong number of arguments expected "+argumentTypes.length+" but found "+args.length;
        }

        matchArgumentExpressionTypes(argumentTypes, args);
        matchGenericArguments(argumentTypes, args);
    }

    function matchArgumentExpressionTypes(argumentTypes, args) {
        for(var i=0;i<argumentTypes.length;i++){
            var expectedType = argumentTypes[i];
            matchSingleArgumentExpressionType(args[i], expectedType, (i+1).toString());
        }
    }

    function matchSingleArgumentExpressionType(argument, expectedType, name) {
        var actualType = getExpressionType(argument);
        if(name==='expression') console.log("EXPECTED:",expectedType,"ACTUAL:",actualType);
        if(!typesMatch(expectedType, actualType)){
            throw "Type error on "+name
        }
    }

    function getExpressionType(expression){
        var actualType = cachedDetermineReturnType(expression);
        //broken
        // if(actualType instanceof ModelDecoratedType){
        //     actualType = ((ModelDecoratedType) actualType).getUndecoratedExpressionType();
        // }
        return actualType;
    }

    // function getRawExpressionType(expression){
    //     return cachedDetermineReturnType(expression);
    // }

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
        const reduceOptional = expression.reduceOptional||false;
        const type = expression.returnType(definition);
        if(baseType(type)!==ReturnTypes.GENERIC) return removeOptional(reduceOptional,type,argumentExpression);
        if(argumentExpression != null){
            const resolvedType = findGenericArgument(argumentExpression);
            return removeOptional(reduceOptional, resolveT(type, resolvedType), argumentExpression);
        }
        throw "Bug!";
    }

    function resolveT(genericType, resolvedType){
        console.log(genericType, resolvedType);
        const rawResolvedType = genericType.replace("GENERIC", baseType(resolvedType)).toLowerCase();
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

    function removeOptional(active, type, argumentExpression){
        if(!active) return type;
        if(argumentExpression==null) return type;
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
        console.log("target",target,"candidate",candidate);
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
            } else if(modelType!==currentModelType){
                throw "Wrong model expected "+modelName+" but found "+currentModelName+" at "+i;
            }
        }
    }

    function baseType(type){
        return ReturnTypes[type.replace('[]', '').replace('{}', '').replace('?', '')];
    }

    function isOptional(type){
        return type.endsWith("?");
    }

    function resolveGenericType(genericType, candidate){
        const rawResolvedType = genericType.replace("GENERIC", baseType(candidate)).toLowerCase();
        return getReturnType(rawResolvedType);
    }

    return {
        isOptional: isOptional,
        test: (expression, expectedType, throwError) =>
        {
            console.log("---------");
            console.log("EXPRESSION", expression);
            try {
                 console.log("RETURN", expression.returnType(definition));
            } catch {
                console.log("RETURN", "????");
            }
            console.log("---------");
            console.log("EXPECTED", expectedType);
            console.log("---------");
            try {
                console.log('>>>>>>>>>START')
                checkTypes(expression);
                console.log('>>>>>>>>>END')
                matchSingleArgumentExpressionType(expression, expectedType, "expression")
                return true;
            } catch (e) {
                if (throwError) throw e;
                console.log(e)
                return false;
            }
        }
    }
}

export function checkExpression (expression, definition, expectedType, throwError){
    var checker = new ExpressionTypeChecker(definition);
    if(!expression) return checker.isOptional(expectedType);
    return checker.test(expression, expectedType,throwError);
}