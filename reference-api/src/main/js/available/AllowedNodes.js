const _ = require('underscore');
let observer = null

var selectedIdHash = null;
var availableNodes = [];
var globalNodeIds = [];


function emitChange() {
    if(observer) observer(availableNodes)
}

export function observe(o) {
    if (observer) {
        throw new Error('Multiple observers not implemented.')
    }

    observer = o
}

export function setGlobalNodes(ids){
    globalNodeIds=ids;
}

export function getGlobalNodes(){
    return globalNodeIds;
}

export function setAvailableNodes(allNodes, selectedIds) {
    selectedIds=selectedIds||[];
    const newSelectedIdHash = selectedIds.join("|");
    // console.log(newSelectedIdHash);
    if(newSelectedIdHash!==selectedIdHash) {
        // console.log("changing");
        availableNodes = _.filter(allNodes, node => selectedIds.includes(node.id));
        selectedIdHash=newSelectedIdHash;
        emitChange()
    }
}


export function getAvailableNodes(){
    return availableNodes;
}