import {Card, Col, Container, Row} from "react-bootstrap";
import {any, arrayOf, shape, string} from "prop-types";
import {canAcceptNode, clearNode, displayName, getNode, hasFocus, setFocus, setNode, slotNodes} from '../model/JsonTemplate';
import EmptySlot from "./EmptySlot";
import ErrorBoundary from "../common/ErrorBoundary";
import Expression from "./Expression";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { ItemTypes } from '../Constants';
import Optional from "../common/Optional";
import Overlay from '../common/Overlay';
import React from 'react';
import {faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {getGlobalNodes} from '../available/AllowNodesProvider';
import { useDrop } from 'react-dnd';

function Slot({node,path,allNodes,forSlot}) {

    function onDrop(item,path){
        const node = item.payload;
        setNode(path,node);
    }
    let acceptTypes = slotNodes(path,forSlot)||getGlobalNodes();

    function dropArea(children, path){

        const [{ isOver, canDrop }, drop] = useDrop({
            accept: ItemTypes.NODE,
            drop: (item) => onDrop(item,path),
            canDrop: (item) => canAcceptNode(item.payload,acceptTypes),
            collect: monitor => ({
                isOver: !!monitor.isOver(),
                canDrop: !!monitor.canDrop(),
            }),
        });

        return (<div
            ref={drop}
            style={{
                position: 'relative'
            }}>{children}
            {isOver && !canDrop && <Overlay color="red" />}
            {!isOver && canDrop && <Overlay color="yellow" />}
            {isOver && canDrop && <Overlay color="green" />}
        </div>);
    }


    function renderArguments(argumentTypes){
        if(!argumentTypes) return null;
        const result = Object.entries(argumentTypes).map(([k,v]) => {
            // let value = v;
            const isOptional = v.endsWith("?");
            const optional = isOptional?<Optional/>:null;
            return (<Row className="mb-2" key={k}><Col sm={"2"}>{k}<br/>{optional}</Col><Col className='font-weight-light'><ErrorBoundary><Expression optional={isOptional} path={path} argumentName={k} type={v}/></ErrorBoundary></Col></Row>);
        });
        return (
            <div>
                <b>Arguments</b>
                <Container>
                    {result}
                </Container>
            </div>
        );
    }

    function renderNodeSlots(nodeSlots){
        if(!nodeSlots) return null;
        const result = Object.entries(nodeSlots).map(([k,v]) => {
            let value = v;
            const optional = v.endsWith("?");
            const name = k;//.endsWith("Node")?k.substr(0,k.length-4):k
            if(optional){
                value=value.substr(0,value.length-1);
            }
            if(value==='limited'){
                const refIds = node.nodeSlotLimits[k];
                const refNodes = allNodes.filter(node=>refIds.includes(node.id));
                value = refNodes.map(i=>i.name).join(", ");
            } else if(value==='*'){
                value = null;
            }
            const childPath = path+"."+k;
            const children = getNode(childPath);
            const slots = children.map((node,i) =>{
                const currentPath = childPath+"."+i;
                return (<Slot key={i} forSlot={name} path={currentPath} node={node} allNodes={allNodes}/>);
            });
            const addSlot =/*dropArea(*/<EmptySlot forSlot={name} optional={optional} path={childPath+".push"} parentPath={path} limit={value}/>;/*, childPath);*/
            return (<Row className="mb-2" key={k}><Col sm>{slots}{addSlot}</Col></Row>);
        });
        return (
            <div>
                <b>Slots</b>
                <Container>
                    {result}
                </Container>
            </div>
        );
    }
    const argumentTypes = renderArguments(node.argumentTypes);
    const nodeSlots = renderNodeSlots(node.nodeSlots);

    function remove(e){
        e.stopPropagation();
        clearNode(path);
    }

    function giveFocus(e){
        e.stopPropagation();
        setFocus(path, null);
    }

    const header = dropArea(<Card.Header  onClick={giveFocus}><h3><b>{node.name}</b> {forSlot?(<span>for {displayName(forSlot)}</span>):null} <div className="float-right remove-container"> <FontAwesomeIcon onClick={remove} className="text-primary h-100" icon={faTimesCircle} /></div></h3></Card.Header>, path);
    return (
        <Card className={"mb-3 "+(hasFocus(path, null)?"border border-primary":"")} >
            {header}
            <Card.Body>
                {argumentTypes}
                {nodeSlots}
            </Card.Body>
        </Card>
    );
}
Slot.propTypes = {
    node: shape({
        name: string,
        argumentTypes: any,
        slotNodes: arrayOf(any),
        nodeSlotLimits: any
    }),
    nodeSlots: any,
    path: string,
    allNodes: arrayOf(any),
    forSlot: string
};
export default Slot;