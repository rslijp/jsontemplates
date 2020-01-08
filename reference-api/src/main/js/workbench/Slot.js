import EmptySlot from "./EmptySlot";

const _ = require('underscore');

import React from 'react';
import Overlay from '../common/Overlay'
import { canAcceptNode, setNode, getNode } from './JsonTemplate'
import { useDrop } from 'react-dnd'
import {Card, Container, Row, Col} from "react-bootstrap";
import { ItemTypes } from '../Constants'
import Optional from "../common/Optional";

function Slot({node,path,allNodes,forSlot}) {

    // console.log("Constructor");

    function onDrop(item,path){
        var node = item.payload;
        setNode(path,node);
    }



    function dropArea(children, path){

        const [{ isOver, canDrop }, drop] = useDrop({
            accept: ItemTypes.NODE,
            drop: (item) => onDrop(item,path),
            canDrop: () => canAcceptNode(),
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
        var result = Object.entries(argumentTypes).map(([k,v]) => {
            let value = v;
            const optional = v.endsWith("?")?<Optional/>:null;
            if(optional){
                value=value.substr(0,value.length-1);
            }
            return (<Row className="mb-2" key={k}><Col sm={"2"}>{k} <br/>{optional}</Col><Col className='font-weight-light'>{value}</Col></Row>);
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
        // console.log("render");
        if(!nodeSlots) return null;
        var result = Object.entries(nodeSlots).map(([k,v]) => {
            let value = v;
            const optional = v.endsWith("?");
            const name = k.endsWith("Node")?k.substr(0,k.length-4):k
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
            // const slot = existing?<Slot key={existing.name} path={childPath} node={existing} allNodes={allNodes}/>:null;
            const slots = children.map((node,i) =>{
                // console.log(node.name);
                return (<Slot key={i} forSlot={name} path={childPath+"."+i} node={node} allNodes={allNodes}/>);
            });
            const addSlot =/*dropArea(*/<EmptySlot forSlot={name} optional={optional} path={childPath+".push"} limit={value}/>/*, childPath);*/
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


    const header = dropArea(<Card.Header><h3>{node.name} {forSlot?(<b>for {forSlot}</b>):null}</h3></Card.Header>, path)

    return (
        <Card className="mb-3">
            {header}
            <Card.Body>
                {argumentTypes}
                {nodeSlots}
            </Card.Body>
        </Card>
    )
}

export default Slot