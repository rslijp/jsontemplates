import _ from 'underscore';
import ParseContext from './ParseContext'
import SuggestionModel from './SuggestionModel';
import SuggestionModelType from './SuggestionModelType';
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

export function parse(text, model, throwException){
    if(!initialized) throw "Not intialized";

    var context = new ParseContext(text)
        .withUnaryLib(UNARY_LOOKUP)
        .withFunctionLib(FUNCTION_LOOKUP)
        .withInfixLib(INFIX_LOOKUP);
    if(text.trim()===""){
        return {success:true, blocks: [], suggestions: new SuggestionModel([
                context.constantSuggestions(),
                context.unarySuggestions(),
                context.functionSuggestions(),
                SuggestionModelType.collectVariableSuggestions(model)
            ])}
    }

    try {
        context.parseExpression(null, true);
        var result = context.yield();
        if (context.empty()) {
            let suggestions = new SuggestionModel();
            validateCompletenessOfArguments(result);
            return {success:true, expression: result===undefined?null:result, blocks: context.getBlocks(),suggestions: suggestions};
        }
    } catch (e){
        if(throwException) throw e;
        let suggestions =  context.getSuggestions();
        const blocks = context.getBlocks();
        var end = 0;
        _.forEach(blocks,(block)=>{
            end = block.end;
        });

        var unparsed=text.substr(end);
        suggestions=suggestions.filterOnPartial(unparsed);
        suggestions.add(e.getSuggestions?e.getSuggestions():null);
        return {success:false, error: e, blocks: context.getBlocks(), suggestions: suggestions};
        // throw e;
    }

    if(throwException) {
        throw "Stack is not empty";
    }
    return {success:false, error: "Stack is not empty", blocks: context.getBlocks(), suggestions: new SuggestionModel()};

}