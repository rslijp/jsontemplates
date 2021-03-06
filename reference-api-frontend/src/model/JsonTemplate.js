/* eslint-disable no-console */
import {head, tail} from "../utils/ArrayUtil";
import {parse} from "./ExpressionParser";

let observer = null;

let slots = [];
let pathWithFocus = null;
let slotWithFocus = null;

function emitChange() {
    if(observer) observer(slots,pathWithFocus);
}

export function observe(o) {
    observer = o;
}

function canMove(path){
    return path.indexOf(".")>0;
}

function moveNode(c,path){
    const dot = path.indexOf(".");
    let head = path, tail=null;
    if(dot>0){
        head=path.substr(0, dot);
        tail=path.substr(dot+1);
    }
    let value = null;
    if(Array.isArray(c)){
        value = c[parseInt(head)];
    } else {
        value = c[head];
    }
    return { value: value, head: head, tail: tail};
}

function innerGetNode(c, path) {
    let {value, tail} = moveNode(c,path);
    if(tail){
        value = innerGetNode(value,tail);
    }
    return value;
}

export function getNode(path) {
    return innerGetNode(slots, path.toString());
}

export function  isArgumentAConstant(path,argumentName, types){
    const parts = argumentName.split(".");
    if(parts.length>1 && head(parts)==='parent'){
        const parentPath=path.split(".");
        parentPath.pop();
        parentPath.pop();
        return isArgumentAConstant(parentPath.join("."),tail(parts).join("."),types);
    }
    if(parts.length>1 && head(parts)==='resolve'){
        argumentName=parts[1];
        const parentPath=path.split(".");
        parentPath.pop();
        parentPath.pop();
        let node = getNode(parentPath.join("."));
        while (node!==null){
            if((node.arguments||{})[argumentName]) break;
            parentPath.pop();
            parentPath.pop();
            node = getNode(parentPath.join("."));
        }
        if(node===null) return null;
        return isArgumentAConstant(parentPath.join("."),argumentName,types);
    }

    types = types || ['text', 'boolean', 'integer','enum'];
    const node = getNode(path);
    const expressionStr = (node.arguments||{})[argumentName];
    if(!expressionStr) {
        const type = (node.argumentTypes||{})[argumentName];
        if(type === undefined){
            console.log(path, argumentName, node);
        }
        const baseType= type.indexOf("?") > -1 ? type.substr(0,type.length-1):type;
        return types.includes(baseType);
    }
    const parseResult = parse(expressionStr);
    if(!parseResult.success) return false;
    const expression  = parseResult.expression;
    if(expression.type!=='CONSTANT') return false;
    if(types) {
        const type = expression.returnType();
        const baseType= type.indexOf("?") > -1 ? type.substr(0,type.length-1):type;
        return types.includes(baseType);
    }
    return true;
}

function formatTextConstant(s){
    if(!s) return s;
    if(!s.match) return s;
    const match = s.match(/'(.*)'/);
    if(!match) return s;
    return match[1];
}

export function getAllowedValues(path,argumentName) {
    const allowedValueSet = getNode(path).allowedValues;

    function extract(field) {
        let value = null;
        if (field && isArgumentAConstant(path, field)) {
            value = getConstantArgumentValue(path, field);
            value = formatTextConstant(value);
        }
        if (value !== null) value = value.toString();
        return value;
    }

    if (allowedValueSet && allowedValueSet[argumentName]) {
        const setForArguments = allowedValueSet[argumentName];
        const discriminatorField = setForArguments.discriminatorField;
        const contextField = setForArguments.contextField;
        let discriminatorValue = extract(discriminatorField);
        let contextValue = extract(contextField);
        const candidate = contextField ?
            setForArguments.valueSet.filter(v => v.context === contextValue && v.discriminator === discriminatorValue) :
            setForArguments.valueSet.filter(v => v.discriminator === discriminatorValue);
        if (candidate.length === 1) {
            return candidate[0].values;
        } else {
            return [];
        }
    } else {
        return null;
    }
}

export function hasAllowedValues(path,argumentName){
    const allowedValueSet = getNode(path).allowedValues;
    return allowedValueSet && allowedValueSet[argumentName];
}

export function  isArgumentAVariable(path,argumentName){
    const node = getNode(path);
    const expressionStr = (node.arguments||{})[argumentName];
    if(!expressionStr) return true;
    const parseResult = parse(expressionStr);
    if(!parseResult.success) return false;
    const expression  = parseResult.expression;
    return (expression.type==='VARIABLE');
}

export function getConstantArgumentValue(path,argumentName,noError){
    const parts = argumentName.split(".");
    if(parts.length>1 && head(parts)==='parent'){
        const parentPath=path.split(".");
        parentPath.pop();
        parentPath.pop();
        return getConstantArgumentValue(parentPath.join("."),tail(parts).join("."),noError);
    }
    if(parts.length>1 && head(parts)==='resolve'){
        argumentName=parts[1];
        const parentPath=path.split(".");
        parentPath.pop();
        parentPath.pop();
        let node = getNode(parentPath.join("."));
        while (node!==null){
            if((node.arguments||{})[argumentName]) break;
            parentPath.pop();
            parentPath.pop();
            node = getNode(parentPath.join("."));
        }
        if(node===null) return null;
        return getConstantArgumentValue(parentPath.join("."),argumentName,noError);
    }

    const node = getNode(path);
    const expressionStr = (node.arguments||{})[argumentName];
    if(!expressionStr) return null;
    const parseResult = parse(expressionStr);
    if(!parseResult.success) return null;
    const expression  = parseResult.expression;
    if(expression.type!=='CONSTANT') {
        if(noError) {
            return null;
        }
        else {
            throw "Not a constant";
        }
    }
    return expression.value;
}


export function getVariableArgumentValue(path,argumentName){
    const node = getNode(path);
    const expressionStr = (node.arguments||{})[argumentName];
    if(!expressionStr) return null;
    const parseResult = parse(expressionStr);
    if(!parseResult.success) return null;
    const expression  = parseResult.expression;
    if(expression.type!=='VARIABLE') throw "Not a variable";
    return expression.name;
}

function innerSetNode(c, path,node) {
    if(canMove(path)) {
        let {value, head, tail} = moveNode(c, path);
        if (tail) {
            return head+"."+innerSetNode(value, tail, node);
        }
    } else {
        const clone = {...node};
        if(clone.nodeSlots){
            Object.keys(clone.nodeSlots).forEach(k=>clone[k]=[]);
        }
        if(Array.isArray(c)){
            if(path==="push"){
                c.push(clone);
                path=(c.length-1).toString();
            } else
            {
                c[parseInt(path)] = clone;
            }
        } else {
            c[path]=clone;
        }
        return path;
    }
}

function updateFocus(path, node){
    slotWithFocus=null;
    if( Object.keys(node.nodeSlots||{}).length>0) {
        pathWithFocus = path;
    }
}

export function setNode(path, node) {
    const actualPath = innerSetNode(slots, path.toString(), node);
    updateFocus(actualPath,node);
    emitChange();
}


function innerClearNode(c, path,node) {
    if(canMove(path)) {
        let {value, tail} = moveNode(c, path);
        if (tail) {
            innerClearNode(value, tail, node);
        }
    } else {
        if(Array.isArray(c)){
            c.splice(parseInt(path), 1);
        } else {
            c[path]=undefined;
        }
    }
}

export function clearNode(path) {
    innerClearNode(slots, path.toString());
    pathWithFocus=null;
    slotWithFocus=null;
    emitChange();
}

export function canAcceptNode(item, allowedItems) {
    return allowedItems.includes(item.id);
}

export function getTemplate(){
    return slots;
}

export function getFocus(){
    return pathWithFocus;
}

export function getFocussedNode(){
    if(!pathWithFocus) return null;
    return getNode(pathWithFocus);
}

export function setFocus(path, slot){
    pathWithFocus=path;
    slotWithFocus=slot;
    emitChange();
}

export function hasFocus(path, slot){
    return pathWithFocus===path && slotWithFocus===slot;
}

function innerSlotNodes(parentPath, slot){
    let parentNode = getNode(parentPath);
    if (parentNode.nodeSlotLimits && parentNode.nodeSlotLimits[slot]) {
        return parentNode.nodeSlotLimits[slot];
    }
    return null;
}
export function slotNodes(parentPath, slot){
    let cPath = parentPath || pathWithFocus;
    let cSlot = slot || slotWithFocus;
    if(cPath) {
        let slotNodes = innerSlotNodes(cPath, cSlot);
        if(slotNodes) return slotNodes;
        if(!cSlot){
            let parts = cPath.split(".");
            if(parts.length>=3){
                cSlot = parts[parts.length-2];
                parts=parts.slice(0,parts.length-2);
                cPath = parts.join(".");
            }
            slotNodes = innerSlotNodes(cPath, cSlot);
        }
        if(slotNodes) return slotNodes;
    }
    return null;
}

export function displayName(slot){
    return slot.endsWith("Node")?slot.substr(0,slot.length-4):slot;
}

function toDTO(slot){
    const dto =  {
        name: slot.name,
        arguments: slot.arguments,
        slots: {

        }
    };
    if(slot.nodeSlots) {
        Object.keys(slot.nodeSlots).forEach(slotName => {
            const name = slotName.endsWith("Node")?slotName.substr(0,slotName.length-4):slotName;
            dto.slots[name] = (slot[slotName] || []).map(toDTO);
        });
    }
    return dto;
}

function fromDTO(nodeDTO,library, expanded){
    const nodeTemplate=library.find(c=>c.name===nodeDTO.name);
    const node =  {...nodeTemplate};
    node.expanded=expanded;
    node.arguments={};
    Object.keys(node.argumentTypes||{}).forEach(argumentName=>{
        node.arguments[argumentName]=nodeDTO.arguments[argumentName];
    });
    Object.keys(node.nodeSlots||{}).forEach(slotName=>{
        const name = slotName.endsWith("Node")?slotName.substr(0,slotName.length-4):slotName;
        const slotsDTO=nodeDTO.slots[name]||[];
        node[slotName]=slotsDTO.map(slot=>fromDTO(slot,library, true));
    });
    return node;
}

export function updateExpression(path, argument, expression){
    const slot = getNode(path);
    if(!slot) return;
    if(!slot.arguments) slot.arguments={};
    slot.arguments[argument]=expression;
}

export function getSlots(){
    return slots;
}


export function getSlotsAsDto(){
    return slots.map(toDTO);
}

export function setSlots(template, library){
    slots=template.slots.map((slot,i)=>{
        return fromDTO(slot,library, i===0);
    });
}