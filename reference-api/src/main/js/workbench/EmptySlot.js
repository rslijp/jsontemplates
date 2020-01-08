import React from 'react';
import Overlay from '../common/Overlay'
import { canAcceptNode, setNode } from './JsonTemplate'
import { useDrop } from 'react-dnd'
import {Jumbotron, Container, Row, Col} from "react-bootstrap";
import { ItemTypes } from '../Constants'
import Optional from "../common/Optional";

function EmptySlot({forSlot, optional, path,limit}) {


    function onDrop(item){
        var node = item.payload;
        setNode(path, node);
    }

    const [{ isOver, canDrop }, drop] = useDrop({
        accept: ItemTypes.NODE,
        drop: (x) => onDrop(x),
        canDrop: () => canAcceptNode(),
        collect: monitor => ({
            isOver: !!monitor.isOver(),
            canDrop: !!monitor.canDrop(),
        }),
    });

    return (
        <div
            ref={drop}
            style={{
                position: 'relative'
            }}
        >
            <Jumbotron>
                <Container>
                    <i>{forSlot?(<b>{forSlot}{optional?<Optional/>:null}: </b>):null}empty slot {limit?"of type(s) "+limit:""}</i>
                    {isOver && !canDrop && <Overlay color="red" />}
                    {!isOver && canDrop && <Overlay color="yellow" />}
                    {isOver && canDrop && <Overlay color="green" />}
                </Container>
            </Jumbotron>
        </div>
    )
}

export default EmptySlot