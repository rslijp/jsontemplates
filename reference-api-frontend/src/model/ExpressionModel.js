import {NodeTypes, OperatorPrecendence, ReturnTypes} from '../Constants';
import {ExceptionWithSuggestion} from './ExceptionWithSuggestion';
import SuggestionModelType from './SuggestionModelType';

let ID = 0;

export function getReturnType(rawType){
    const transformed = rawType.replace("T","GENERIC").toUpperCase().replace("?","OPTIONAL").replace("*","LIST").replace("[]","MAP");
    return ReturnTypes[transformed];
}

export function Constant(value, type){
    return {
        id: ID++,
        type: NodeTypes.CONSTANT,
        value: value,
        priority: ()=>OperatorPrecendence.CONSTANTS,
        returnType: ()=>{
            return type;
        }
    };
}

export function Variable(name){
    return {
        id: ID++,
        type: NodeTypes.VARIABLE,
        name: name,
        priority: ()=>OperatorPrecendence.VARIABLE,
        returnType: (model)=>{
            if(model== null){
                throw "no model";
            }
            const {propertyDescriptions} = model;
            const hit = propertyDescriptions.find(d => d.name === name);
            if(!hit) throw new ExceptionWithSuggestion("No such property "+name, SuggestionModelType.collectVariableSuggestions(model).filterOnPartial(name));
            if(!hit.readable) throw new ExceptionWithSuggestion( "Can't read property "+name, SuggestionModelType.collectVariableSuggestions(model).filterOnPartial(name));
            return getReturnType(hit.type); //broken decorateType
        }
    };
}

export function Brackets(){
    return {
        id: ID++,
        type: NodeTypes.BRACKETS,
        arguments: [],
        argumentsTypes: [ReturnTypes.GENERIC],
        priority: ()=>OperatorPrecendence.BRACKETS,
        returnType: (model)=>{
            if(model== null){
                throw "no model";
            }
            return ReturnTypes.GENERIC;
        }
    };
}

export function Ternary(){
    const args = [];
    return {
        id: ID++,
        type: NodeTypes.TERNARY,
        arguments: args,
        argumentsTypes: [ReturnTypes.BOOLEAN, ReturnTypes.GENERIC, ReturnTypes.GENERIC],
        priority: ()=> args.length>=2?OperatorPrecendence.FUNCTION:OperatorPrecendence.TERNARY,
        returnType: (model)=>{
            if(model== null){
                throw "no model";
            }
            return ReturnTypes.GENERIC;
        }
    };
}


export function createExpression(type){
    const args = [];
    const argumentsTypes = type.argumentTypes.map(t=>getReturnType(t));
    return {
        id: ID++,
        type: type.name,
        arguments: args,
        argumentsTypes: argumentsTypes,
        reduceOptional: type.reduceOptional,
        downCastIfPossible: type.downCastIfPossible,
        priority: ()=>type.priority,
        returnType: ()=>{
            return getReturnType(type.returnType);
        }
    };
}