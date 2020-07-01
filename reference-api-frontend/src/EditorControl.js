import {post,revert} from './client';
import {Button} from "react-bootstrap";
import React from 'react';
import {any, arrayOf, func, string} from "prop-types";
import {getSlotsAsDto, setSlots} from "./model/JsonTemplate";

function EditorControl({id, cancelUrl, commitUrl, allNodes,onWorkBenchAvailable}){
    const onClose = ()=>{
        document.location = cancelUrl;
    };

    const onCommit = ()=>{
        const slots = getSlotsAsDto();
        post(id,{slots: slots},data => {
            if(!data) {
                alert('Save failed');
            } else {
                document.location = commitUrl;
            }
        });
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
    id: string,
    commitUrl: string,
    cancelUrl: string,
    allNodes: arrayOf(any),
    onWorkBenchAvailable: func
};
export default EditorControl;