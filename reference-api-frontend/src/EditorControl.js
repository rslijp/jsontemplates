import {post,revert} from './client';
import {Button} from "react-bootstrap";
import React from 'react';
import {any, arrayOf, func} from "prop-types";
import {getSlotsAsDto, setSlots} from "./model/JsonTemplate";

function EditorControl({allNodes,onWorkBenchAvailable}){
    const onCommit = ()=>{
        const slots = getSlotsAsDto();
        post(document.location.hash.substring(1),{slots: slots},data => {
            if(!data) alert('Save failed');
        });
    };
    const onClose = ()=>{
        window.close();
    };
    const onRevert = ()=>{
        if(onWorkBenchAvailable) onWorkBenchAvailable(false);
        revert(document.location.hash.substring(1),data => {
            setSlots(data,allNodes);
            if(onWorkBenchAvailable) onWorkBenchAvailable(true);
        });
    };
    return <div className={"editControls"}>
        <Button variant="primary" onClick={onCommit}>Commit</Button>{' '}
        <Button variant="secondary" onClick={onClose}>Close</Button>{' '}
        <Button variant="warning" onClick={onRevert}>Revert</Button>
    </div>

}
EditorControl.propTypes = {
    allNodes: arrayOf(any),
    onWorkBenchAvailable: func
};
export default EditorControl;