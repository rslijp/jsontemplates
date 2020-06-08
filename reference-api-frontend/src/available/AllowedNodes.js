let observer = null;

let selectedIdHash = null;
let availableNodes = [];
let globalNodeIds = [];


function emitChange() {
    if(observer) observer(availableNodes);
}

export function observe(o) {
    if (observer) {
        throw new Error('Multiple observers not implemented.');
    }
    observer = o;
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
    if(newSelectedIdHash!==selectedIdHash) {
        availableNodes = allNodes.filter(node => selectedIds.includes(node.id));
        selectedIdHash=newSelectedIdHash;
        emitChange();
    }
}


export function getAvailableNodes(){
    return availableNodes;
}