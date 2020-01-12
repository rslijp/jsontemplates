import _ from 'underscore';
import {NodeTypes, ReturnTypes,OperatorPrecendence} from '../Constants';

function getReturnType(rawType){
    return ReturnTypes[rawType.toUpperCase().replace("?","OPTIONAL")]
}

export function Constant(value, type){
    return {
        type: NodeTypes.CONSTANT,
        value: value,
        priority: ()=>OperatorPrecendence.CONSTANTS,
        returnType: ()=>{
            return type;
        }
    }
}
export function Variable(name){
    return {
        type: NodeTypes.VARIABLE,
        name: name,
        priority: ()=>OperatorPrecendence.VARIABLE,
        returnType: (model)=>{
            if(model== null){
                throw "no model";
            }
            return model.definition[name].decoratedType;
        }
    }
}

export function Brackets(){
    return {
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
    }
}

export function Ternary(){
    const args = [];
    return {
        type: NodeTypes.TERNARY,
        arguments: args,
        argumentsTypes: [ReturnTypes.BOOLEAN, ReturnTypes.GENERIC, ReturnTypes.GENERIC],
        priority: ()=>args.length>=2?OperatorPrecendence.FUNCTION:OperatorPrecendence.TERNARY,
        returnType: (model)=>{
            if(model== null){
                throw "no model";
            }
            return ReturnTypes.GENERIC;
        }
    }
}


export function createExpression(type){
    console.log("CREATE OF ",type)
    const args = [];
    const argumentsTypes = _.map(type.argumentTypes, t=>getReturnType(t));
    return {
        type: type.name,
        arguments: args,
        argumentsTypes: argumentsTypes,
        priority: ()=>type.priority,
        returnType: (model)=>{
            return getReturnType(type.returnType);
        }
    }
}