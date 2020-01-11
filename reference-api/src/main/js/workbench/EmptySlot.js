import {ItemTypes} from "../Constants";

const _ = require('underscore');

import React from 'react';
import Overlay from '../common/Overlay'
import { canAcceptNode, setNode, setFocus,hasFocus, slotNodes, displayName} from '../model/JsonTemplate'
import { useDrop } from 'react-dnd'
import {Jumbotron, Container, Row, Col} from "react-bootstrap";
import Optional from "../common/Optional";
import {getGlobalNodes} from "../available/AllowedNodes";

function EmptySlot({forSlot, optional, path, parentPath, limit}) {

    function onDrop(item){
        var node = item.payload;
        setNode(path, node);
    }

    var acceptedNodes = slotNodes(parentPath,forSlot)||getGlobalNodes();

    const [{ isOver, canDrop }, drop] = useDrop({
        accept: ItemTypes.NODE,
        drop: (item) => onDrop(item),
        canDrop: (item) => canAcceptNode(item.payload,acceptedNodes),
        collect: monitor => ({
            isOver: !!monitor.isOver(),
            canDrop: !!monitor.canDrop(),
        }),
    });

    function giveFocus(){
        setFocus(parentPath, forSlot)
    }
    return (
        <div
            ref={drop}
            style={{
                position: 'relative'
            }}
        >
            <Jumbotron onClick={giveFocus} className={hasFocus(parentPath, forSlot)?"border border-primary":""}>
                <Container>
                    <i>{forSlot?(<b>{displayName(forSlot)}{optional?<Optional/>:null}: </b>):null}empty slot {limit?"of type(s) "+limit:""}</i>
                    {isOver && !canDrop && <Overlay color="red" />}
                    {!isOver && canDrop && <Overlay color="yellow" />}
                    {isOver && canDrop && <Overlay color="green" />}
                </Container>
            </Jumbotron>
        </div>
    )
}

export default EmptySlot