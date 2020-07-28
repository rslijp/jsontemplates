import {post,revert} from './client';
import {Button, Form, InputGroup} from "react-bootstrap";
import React from 'react';
import {any, arrayOf, func, string} from "prop-types";
import {getSlotsAsDto, setSlots} from "./model/JsonTemplate";
import {faDownload, faUpload, faSave, faHistory, faWindowClose} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

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

    const onDownload = ()=>{
        const slots = getSlotsAsDto();
        const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(slots));
        const downloadAnchorNode = document.createElement('a');
        downloadAnchorNode.setAttribute("href",     dataStr);
        downloadAnchorNode.setAttribute("download", "workbench.json");
        document.body.appendChild(downloadAnchorNode); // required for firefox
        downloadAnchorNode.click();
        downloadAnchorNode.remove();
    };

    const onUpload = (e)=>{
        const file = e.target.files[0];
        if(!file) {
            alert('No file')
            return;
        }
        const sizeInMb = file.size/1024/1024;
        if(sizeInMb>5) {
            alert('File to big')
            return;
        }
        const reader = new FileReader();
        reader.readAsText(file);
        if(onWorkBenchAvailable) onWorkBenchAvailable(false);
        reader.onload = function() {
            console.log(reader.result);
            const data = JSON.parse(reader.result);
            setSlots({slots: data},allNodes);
            if(onWorkBenchAvailable) onWorkBenchAvailable(true);
        };
        reader.onerror = function() {
            alert('Failed to upload')
        };
    };

    const onRevert = ()=>{
        if(onWorkBenchAvailable) onWorkBenchAvailable(false);
        revert(document.location.hash.substring(1),data => {
            console.log(data);
            setSlots(data,allNodes);
            if(onWorkBenchAvailable) onWorkBenchAvailable(true);
        });
    };
    return <div className={"editControls"}>
        <Button variant="primary" title={"Commit"} onClick={onCommit}><FontAwesomeIcon icon={faSave}/></Button>{' '}
        <Button variant="secondary"  title={"Close"} onClick={onClose}><FontAwesomeIcon icon={faWindowClose}/></Button>{' '}
        <Button variant="warning"  title={"Revert"} onClick={onRevert}><FontAwesomeIcon icon={faHistory}/></Button>{' '}
        <Button variant="outline-secondary" title={"Download"} onClick={onDownload}><FontAwesomeIcon icon={faDownload}/></Button>{' '}
            <InputGroup size="sm" >
                <InputGroup.Prepend variant="outline-secondary">
                    <InputGroup.Text  variant="outline-secondary">{" "}<FontAwesomeIcon icon={faUpload}/>{" "}</InputGroup.Text>
                </InputGroup.Prepend>
                <Form.File
                    id={"file-upload"}
                    label={"upload"}
                    custom
                    onChange={onUpload}
                />
            </InputGroup>
        {/*<Button variant="outline-secondary" title={"Upload"} onClick={onDownload}><FontAwesomeIcon icon={faUpload}/></Button>{' '}*/}
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