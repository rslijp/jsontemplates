import EmptySlot from "./EmptySlot";
import _ from 'underscore';
import React from 'react';
import Overlay from '../common/Overlay'
import {canAcceptNode, setNode, getNode, clearNode, setFocus, hasFocus, displayName, slotNodes} from '../model/JsonTemplate'
import {getGlobalNodes} from './../available/AllowedNodes'
import { useDrop } from 'react-dnd'
import {Card, Container, Row, Col} from "react-bootstrap";
import { ItemTypes } from '../Constants'
import Optional from "../common/Optional";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import Expression from "./Expression";

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
            return (<Row className="mb-2" key={k}><Col sm={"2"}>{k}<br/>{optional}</Col><Col className='font-weight-light'><Expression optional={isOptional} type={v}/></Col></Row>);
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
                const refNodes = _.filter(allNodes, node=>refIds.includes(node.id));
                value = _.pluck(refNodes,"name").join(", ");
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
    )
}

export default Slot