import {Card, Col, Container, Row} from "react-bootstrap";
import React, {useState} from 'react';
import {any, arrayOf, shape, string} from "prop-types";
import {canAcceptNode, clearNode, displayName, getNode, hasFocus, setFocus, setNode, slotNodes} from '../model/JsonTemplate';

import {faMinusSquare, faPlusSquare, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import Argument from "./Argument";
import EmptySlot from "./EmptySlot";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { ItemTypes } from '../Constants';
import Overlay from '../common/Overlay';
import {getGlobalNodes} from '../available/AllowNodesProvider';
import { useDrop } from 'react-dnd';

function Slot({node,path,allNodes,forSlot}) {
    const [expanded, setExpanded] = useState(node.expanded===undefined?true:node.expanded);
    const toggleExpanded = ()=>{
        setExpanded(!expanded);
    };
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
            return <Argument key={k} optional={isOptional} path={path} argumentName={k} type={v}/>;
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
            const name = k.endsWith("Node")?k.substr(0,k.length-4):k;
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
            const addSlot =/*dropArea(*/<EmptySlot forSlot={name+"Node"} optional={optional} path={childPath+".push"} parentPath={path} limit={value}/>;/*, childPath);*/
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
    const argumentTypes = expanded?renderArguments(node.argumentTypes):null;
    const nodeSlots = expanded?renderNodeSlots(node.nodeSlots):null;

    function remove(e){
        e.stopPropagation();
        clearNode(path);
    }

    function giveFocus(e){
        e.stopPropagation();
        setFocus(path, null);
    }

    function nodeName(){
        const args = (node.arguments||{});
        const name = args[node.namingField||'name'];
        return node.name+(name?(' '+name):'');
    }
    const header = dropArea(<Card.Header  onClick={giveFocus}><h3><b>{nodeName()}</b> {forSlot?(<span>for {displayName(forSlot)}</span>):null}
        <div className="toggle-container"><FontAwesomeIcon onClick={toggleExpanded} className="text-secondary h-100" icon={expanded?faMinusSquare:faPlusSquare} /></div>
        <div className="float-right remove-container"><FontAwesomeIcon onClick={remove} className="text-primary h-100" icon={faTimesCircle} /></div>
    </h3></Card.Header>, path);
    return (
        <Card className={"mb-3 "+(hasFocus(path, null)?"border border-primary":"")} >
            {header}
            <Card.Body className={expanded?"":"d-none"}>
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