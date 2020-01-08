import React, { useState } from 'react';
import Slot from "./Slot";
import EmptySlot from "./EmptySlot";
import {observe, getTemplate} from "./JsonTemplate";

function WorkBench({allNodes}) {

    function renderSlots(){
        return getTemplate().map((node,i) =>{
            console.log(node.name);
            return (<Slot key={node.name} path={i} node={node} allNodes={allNodes}/>);
        });
    }

    const [slots, setSlots] = useState(
        renderSlots()
    );

    function updateState(slots){
        console.log("UPDATE STATE")
        setSlots(renderSlots());
    }
    console.log("XX");
    observe(updateState.bind(this));

    return (
        <div className={"workBench"}>
            <h2>Workbench</h2>
            {slots}
            <EmptySlot path="push"/>
        </div>
    );
}

export default WorkBench