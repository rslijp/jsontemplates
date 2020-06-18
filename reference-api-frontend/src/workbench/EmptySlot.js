import {Container, Jumbotron} from "react-bootstrap";
import {any, arrayOf, bool, string} from "prop-types";
import { canAcceptNode, displayName, hasFocus,setFocus, setNode, slotNodes} from '../model/JsonTemplate';
import {ItemTypes} from "../Constants";
import Optional from "../common/Optional";
import Overlay from '../common/Overlay';
import React from 'react';
import {getGlobalNodes} from "../available/AllowNodesProvider";
import { useDrop } from 'react-dnd';

function EmptySlot({forSlot, optional, path, parentPath, limit}) {

    function onDrop(item){
        const node = item.payload;
        setNode(path, node);
    }

    const acceptedNodes = slotNodes(parentPath, forSlot) || getGlobalNodes();

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
        setFocus(parentPath, forSlot);
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
    );
}
EmptySlot.propTypes = {
    path: string,
    parentPath: string,
    allNodes: arrayOf(any),
    limit: string,
    optional: bool,
    forSlot: string
};
export default EmptySlot;