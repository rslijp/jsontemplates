import {setAvailableNodes} from "../available/AllowedNodes";

const _ = require('underscore');
let observer = null

var slots = [];
var pathWithFocus = null;
var slotWithFocus = null;

function emitChange() {
    if(observer) observer(slots,pathWithFocus)
}

export function observe(o) {
    if (observer) {
        throw new Error('Multiple observers not implemented.')
    }

    observer = o
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
    if(_.isArray(c)){
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

function innerSetNode(c, path,node) {
    if(canMove(path)) {
        let {value, head, tail} = moveNode(c, path);
        if (tail) {
            return head+"."+innerSetNode(value, tail, node);
        }
    } else {
        const clone = _.clone(node);
        if(clone.nodeSlots){
            _.keys(clone.nodeSlots).forEach(k=>clone[k]=[]);
        }
        if(_.isArray(c)){
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
    if( _.keys(node.nodeSlots).length>0) {
        pathWithFocus = path;
    }
}

export function setNode(path, node) {
    const actualPath = innerSetNode(slots, path.toString(), node);
    updateFocus(actualPath,node);
    emitChange()
}


function innerClearNode(c, path,node) {
    if(canMove(path)) {
        let {value, tail} = moveNode(c, path);
        if (tail) {
            innerClearNode(value, tail, node);
        }
    } else {
        if(_.isArray(c)){
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
    emitChange()
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