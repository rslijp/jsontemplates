import {Button, Container} from "react-bootstrap";
import React, {useState} from "react";
import NodeList from "./available/NodeList";
import WorkBench from "./workbench/WorkBench";
import AppInitialized from "./utils/AppInitialized";
import {DndProvider} from "react-dnd";
import Backend from "react-dnd-html5-backend";
import {any} from "prop-types";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft, faAngleRight, faExchangeAlt, faCompressAlt } from "@fortawesome/free-solid-svg-icons";
import VariablesList from "./variables/VariablesList";
import EditorControl from "./EditorControl";

function MainApp({nodeDescriptions}){
    const [leftExpanded, setLeftExpanded] = useState(true);
    const [sticky, setSticky] = useState(true);
    const [rightExpanded, setRightExpanded] = useState(false);
    const [workBenchAvailable, setWorkBenchAvailable] = useState(true);
    const toggleLeftExpand = ()=>{
        const update = !leftExpanded;
        setLeftExpanded(update);
    };
    const toggleRightExpand = ()=>{
        const update = !rightExpanded;
        setRightExpanded(update);
    };
    const toggleSticky = ()=>{
        const update = !sticky;
        setSticky(update);
    };
    let benchClasses = "";
    if(sticky){
        benchClasses+= (leftExpanded?" workbench-shrink-left":"");
        benchClasses+= (rightExpanded?" workbench-shrink-right":"");

    }
    const updateWorkBenchAvailable=(state)=>{
        setWorkBenchAvailable(state);
    }
    const workbench = (
        <Container id="root" fluid={true}>
            <div className={"leftmenu "+(leftExpanded?"leftmenu-expanded":"")}>
                <h2>Available <Button variant="light" className={"expand-button"} onClick={toggleLeftExpand}><FontAwesomeIcon title={leftExpanded?"hide":"expand"} icon={leftExpanded?faAngleLeft:faAngleRight}/></Button></h2>
                <div className="card-container">
                <NodeList allNodes={nodeDescriptions}/>
                </div>
            </div>
            <div className={"workbench"+benchClasses}>
                <h2>Workbench <Button variant="light" className={"expand-button"} onClick={toggleSticky}><FontAwesomeIcon title={sticky?"shrink":"expand"} icon={sticky?faExchangeAlt:faCompressAlt}/></Button>
                    <EditorControl allNodes={nodeDescriptions} onWorkBenchAvailable={updateWorkBenchAvailable}/>
                </h2>
                {workBenchAvailable?<WorkBench allNodes={nodeDescriptions} />:null}
            </div>
            <div className={"rightmenu "+(rightExpanded?"rightmenu-expanded":"")}>
                <h2><Button variant="light" className={"expand-button"} onClick={toggleRightExpand}><FontAwesomeIcon title={rightExpanded?"expand":"hide"} icon={rightExpanded?faAngleRight:faAngleLeft}/></Button> Model</h2>
                <div className="card-container">
                    <VariablesList/>
                </div>
            </div>
        </Container>
    );
    return (<AppInitialized>
        <DndProvider backend={Backend}>{workbench}</DndProvider>
    </AppInitialized>);
}
MainApp.propTypes = {
    nodeDescriptions: any
};
export default MainApp;