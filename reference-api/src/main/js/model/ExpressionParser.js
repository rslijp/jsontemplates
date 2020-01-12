import _ from 'underscore';
import ParseContext from './ParseContext'
import {validateCompletenessOfArguments} from "./ParseUtil";

let initialized= false;
const FUNCTION_LOOKUP = {};
const INFIX_LOOKUP = {};
const UNARY_LOOKUP = {};


export function initExpressionLibrary(availableExpressions){
    _.forEach(availableExpressions, expression=>{
        if(expression.parseType==="CONSTANT") return;
        if(expression.parseType==="UNARY") UNARY_LOOKUP[expression.operator]=expression;
        if(expression.parseType==="FUNCTION") FUNCTION_LOOKUP[expression.operator]=expression;
        if(expression.parseType==="INFIX") INFIX_LOOKUP[expression.operator]=expression;
    });
    initialized=true;
}

export function parse(text, throwException){
    if(!initialized) throw "Not intialized";
    var context = new ParseContext(text)
        .withUnaryLib(UNARY_LOOKUP)
        .withFunctionLib(FUNCTION_LOOKUP)
        .withInfixLib(INFIX_LOOKUP);
    try {
        context.parseExpression(null);
        var result = context.yield();
        if (context.empty()) {
            validateCompletenessOfArguments(result);
            return {success:true, result: result===undefined?null:result};
        }
    } catch (e){
        if(throwException) throw e;
        console.log(e);
        return {success:false, error: e};
        // throw e;
    }

    if(throwException) {
        throw "Stack is not empty";
    }
    return {success:false, error: "Stack is not empty"};;

}