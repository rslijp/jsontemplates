import React from 'react';
import {Button} from "react-bootstrap";
import {getSlotsAsDto} from "./model/JsonTemplate";
import {post} from './client';

function EditorControl({}){
    const onCommit = ()=>{
        const slots = getSlotsAsDto();
        post(document.location.hash.substring(1),{slots: slots},data => {
            console.log(data);
        });
    };
    const onClose = ()=>{
        window.close();
    };
    const onRevert = ()=>{
        window.close();
    };
    return <div className={"editControls"}>
        <Button variant="primary" onClick={onCommit}>Commit</Button>{' '}
        <Button variant="secondary" onClick={onClose}>Close</Button>{' '}
        <Button variant="warning" onClick={onRevert}>Revert</Button>
    </div>

}
export default EditorControl;