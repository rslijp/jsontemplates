import React, {useState} from 'react';
import {any} from "prop-types";

let selectedIdHash = null;
let globalNodeIds = [];
let setAvailableNodesHook = null;

function setGlobalNodes(ids){
    globalNodeIds=ids;
}

function getGlobalNodes(){
    return globalNodeIds;
}

function setAvailableNodes(allNodes, selectedIds) {
    selectedIds=selectedIds||[];
    const newSelectedIdHash = selectedIds.join("|");
    if(newSelectedIdHash!==selectedIdHash) {
        selectedIdHash=newSelectedIdHash;
        setAvailableNodesHook(allNodes.filter(node => selectedIds.includes(node.id)));
    }
}

const allowNodesContext = React.createContext([{}, () => {}]);
function AllowNodesProvider({ children }) {
    const [availableNodes, _setAvailableNodes] = useState({});
    setAvailableNodesHook=_setAvailableNodes.bind(this);
    return (
        <allowNodesContext.Provider value={[availableNodes, _setAvailableNodes]}>
            {children}
        </allowNodesContext.Provider>
    );
}
AllowNodesProvider.propTypes = {
    children: any
};
export {
    allowNodesContext as AllowNodesContext,
    AllowNodesProvider,
    setAvailableNodes,
    setGlobalNodes,
    getGlobalNodes
};