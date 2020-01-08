const _ = require('underscore');
let observer = null

var slots = [];


function emitChange() {
    if(observer) observer(slots)
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
    return { value: value, tail: tail};
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
        let {value, tail} = moveNode(c, path);
        if (tail) {
            innerSetNode(value, tail, node);
        }
    } else {
        const clone = _.clone(node);
        console.log(clone);
        if(clone.nodeSlots){
            _.keys(clone.nodeSlots).forEach(k=>clone[k]=[]);
        }
        if(_.isArray(c)){
            if(path==="push"){
                c.push(clone);
            } else
            {
                c[parseInt(path)] = clone;
            }
        } else {
            c[path]=clone;
        }
    }
}


export function setNode(path, node) {
    console.log(path);
    innerSetNode(slots, path.toString(), node);
    emitChange()
}

export function canAcceptNode() {
   return true;
}

export function getTemplate(){
    return slots;
}