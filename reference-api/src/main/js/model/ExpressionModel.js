import _ from 'underscore';
import {NodeTypes, ReturnTypes,OperatorPrecendence} from '../Constants';

let ID = 0;

export function getReturnType(rawType){
    //dodgy
    return ReturnTypes[rawType.replace("T","GENERIC").toUpperCase().replace("?","OPTIONAL")]
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
    }
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
            var hit = _.findWhere(model.propertyDescriptions, {name: name});
            if(!hit) throw "No such property "+name;
            console.log(hit);
            if(!hit.readable) throw "Can't read property "+name;
            return getReturnType(hit.type); //broken decorateType
        }
    }
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
    }
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
    }
}


export function createExpression(type){
    const args = [];
    const argumentsTypes = _.map(type.argumentTypes, t=>getReturnType(t));
    return {
        id: ID++,
        type: type.name,
        arguments: args,
        argumentsTypes: argumentsTypes,
        reduceOptional: type.reduceOptional,
        downCastIfPossible: type.downCastIfPossible,
        priority: ()=>type.priority,
        returnType: (model)=>{
            return getReturnType(type.returnType);
        }
    }
}